package serializer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsedresult.ParsedResult;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DynamoDbSerializer {

    private static final Logger log = LoggerFactory.getLogger(DynamoDbSerializer.class);
    private Properties appProps;
    private BasicAWSCredentials awsCredentials;
    private String tableName = "tennislink_tournaments";

    public DynamoDbSerializer() {
        InputStream configResourceStream = getClass().getClassLoader().getResourceAsStream("aws.properties");
        appProps = new Properties();
        try {
            appProps.load(configResourceStream);
        } catch (IOException e) {
            log.error(e.toString());
        }

        awsCredentials = new BasicAWSCredentials(
                appProps.getProperty("access_key"),
                appProps.getProperty("secret_key")
        );
    }

    public void initializeTable() {
        AmazonDynamoDB client;
        client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);



        Stream<Table> tableStream = StreamSupport.stream(dynamoDB.listTables().spliterator(), false);
        if(tableStream.anyMatch(t -> t.getTableName().equalsIgnoreCase(tableName))) {
            Table forDelete = dynamoDB.getTable(tableName);
            forDelete.delete();
            try {
                forDelete.waitForDelete();
            } catch (InterruptedException e) {
                log.error(e.toString());
            }
        }

        Table table = dynamoDB.createTable(tableName,
                Arrays.asList(
                        new KeySchemaElement("tournamentId", KeyType.HASH), // Partition key
                        new KeySchemaElement("tournamentName", KeyType.RANGE)), // Sort key
                Arrays.asList(
                        new AttributeDefinition("tournamentId", ScalarAttributeType.S),
                        new AttributeDefinition("tournamentName", ScalarAttributeType.S)
                ),
                new ProvisionedThroughput(10L, 10L));
        try {
            table.waitForActive();
        } catch (InterruptedException e) {
            log.error(e.toString());
        }
    }

    public void serializeParsedResult(ParsedResult result) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);
        Table tournamentTable = dynamoDB.getTable(tableName);

        result.getParsedTournamentList().stream().forEach(tournament -> {
            Item tournamentItem = new Item().withPrimaryKey("tournamentId",tournament.getTournamentId());

            List<ObjectKeyValue> objectPropsKeyValues = getObjectPropsKeyValues(tournament);
            for (ObjectKeyValue kv : objectPropsKeyValues ) {
                if(kv.getValue() != null && StringUtils.isNotEmpty(kv.getValue())) {
                    tournamentItem.withString(kv.getKey(), kv.getValue());
                }
            }

            tournamentTable.putItem(tournamentItem);
        });
    }

    public List<ObjectKeyValue> getObjectPropsKeyValues(Object bean) {
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(bean.getClass(), Object.class);
        } catch (IntrospectionException e) {
            log.error(e.toString());
        }

        PropertyDescriptor[] props = info.getPropertyDescriptors();
        return Arrays.stream(props)
                .filter(p -> !p.getName().contains("tournamentId"))
                .map(pd -> {
            String name = pd.getName();
            Method getter = pd.getReadMethod();
            Object value = null;
            try {
                value = getter.invoke(bean);
            } catch (IllegalAccessException e) {
                log.error(e.toString());
            } catch (InvocationTargetException e) {
                log.error(e.toString());
            }

            ObjectKeyValue kv = new ObjectKeyValue();
            kv.setKey(name);
            kv.setValue(value == null ? null : value.toString());
            return kv;
        }).collect(Collectors.toList());
    }
}
