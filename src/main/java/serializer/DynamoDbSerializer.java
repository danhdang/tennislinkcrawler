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
import org.joda.time.DateTime;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DynamoDbSerializer {

    private static final Logger log = LoggerFactory.getLogger(DynamoDbSerializer.class);
    private Properties appProps;
    private BasicAWSCredentials awsCredentials;
    private String tableName = "tennislink_tournaments";
    private JsonSerializer jsonSerializer = new JsonSerializer();

    public DynamoDbSerializer() {
        InputStream configResourceStream = getClass().getClassLoader().getResourceAsStream("secret.properties");
        appProps = new Properties();
        try {
            appProps.load(configResourceStream);
        } catch (IOException e) {
            log.error(e.toString());
        }

        awsCredentials = new BasicAWSCredentials(
                appProps.getProperty("aws_access_key"),
                appProps.getProperty("aws_secret_key")
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

            log.info("Saving tournament to DynamoDb: " + jsonSerializer.serialize(tournament) );

            Item tournamentItem = new Item().withPrimaryKey("tournamentId",tournament.getTournamentId());
            tournamentItem.withString("lastUpdated", DateTime.now().toString());

            List<PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(tournament);
            propertyDescriptors.forEach(pd -> {

                String name = pd.getName();
                Class<?> propertyType = pd.getPropertyType();
                Method getter = pd.getReadMethod();
                Object value = null;
                try {
                    value = getter.invoke(tournament);
                } catch (IllegalAccessException e) {
                    log.error(e.toString());
                } catch (InvocationTargetException e) {
                    log.error(e.toString());
                }

                if(name.contains("tournamentId")) {
                    return;
                }

                if(value == null) {
                    return;
                }

                Class<?> mapClass = Map.class;
                if(propertyType.getName().equalsIgnoreCase(mapClass.getName())) {
                    Map<String, ?> stringMap = (Map<String, ?>) value;
                    if(stringMap.size() > 0) {
                        tournamentItem.withMap(name, stringMap);
                    }
                }

                Class<?> arrayClass = String[].class;
                if(propertyType.getName().equalsIgnoreCase(arrayClass.getName())) {
                    String[] stringArray = (String[]) value;
                    if(stringArray.length > 0) {
                        tournamentItem.withStringSet(name, new HashSet<>(Arrays.asList(stringArray)));
                    }
                }

                Class<?> integerClass = Integer.class;
                if(propertyType.getName().equalsIgnoreCase(integerClass.getName())) {
                    tournamentItem.withInt(name, (Integer) value);
                }

                Class<?> stringClass = String.class;
                if(propertyType.getName().equalsIgnoreCase(stringClass.getName())) {
                    String stringVal = (String) value;
                    if(StringUtils.isNotEmpty(stringVal)) {
                        tournamentItem.withString(name, stringVal);
                    }
                }
            });

            tournamentTable.putItem(tournamentItem);
        });
    }

    public List<PropertyDescriptor> getPropertyDescriptors(Object bean) {
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(bean.getClass(), Object.class);
        } catch (IntrospectionException e) {
            log.error(e.toString());
        }

        PropertyDescriptor[] props = info.getPropertyDescriptors();
        return Arrays.stream(props)
                .filter(p -> !p.getName().contains("tournamentId"))
                .collect(Collectors.toList());
    }
}
