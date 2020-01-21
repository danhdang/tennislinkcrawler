import org.junit.Test;
import tennislink.crawler.models.ParsedResult;
import tennislink.crawler.models.ParsedTournament;
import tennislink.crawler.serializers.DynamoDbSerializer;
import tennislink.crawler.serializers.ExcelFileSerializer;
import tennislink.crawler.serializers.JsonSerializer;


public class SerializerTest extends BaseTest {

    @Test
    public void testDynamnoDbSerializer() {
        ParsedResult result = new ParsedResult();
        ParsedTournament tournament1 = new ParsedTournament();
        tournament1.setTournamentId("123123123123");
        tournament1.setTournamentName("This is my tournament");
        tournament1.setChecksPayableTo("Payable to me");
        tournament1.setCity("San Diego, CA");
        tournament1.setTournamentId("123123123123");
        tournament1.setDirectorCell("671-123-1231");
        tournament1.setDirectorName("Mr. Director");
        tournament1.setDivisions(new String[] { "division 1", "division 2"});
        result.add(tournament1);

        DynamoDbSerializer serializer = new DynamoDbSerializer();
        serializer.serializeParsedResult(result);
    }

    @Test
    public void testJsonSerializer() {
        ParsedResult result = new ParsedResult();
        ParsedTournament tournament1 = new ParsedTournament();
        tournament1.setTournamentId("123123123123");
        tournament1.setTournamentName("This is my tournament");
        tournament1.setChecksPayableTo("Payable to me");
        tournament1.setCity("San Diego, CA");
        tournament1.setTournamentId("123123123123");
        tournament1.setDirectorCell("671-123-1231");
        tournament1.setDirectorName("Mr. Director");
        tournament1.setDivisions(new String[] { "division 1", "division 2"});
        result.add(tournament1);

        JsonSerializer serializer = new JsonSerializer();
        serializer.serializeToFiles(result);
    }

    @Test
    public void testExcelSerializer() {
        ParsedResult result = new ParsedResult();
        ParsedTournament tournament1 = new ParsedTournament();
        tournament1.setTournamentId("123123123123");
        tournament1.setTournamentName("This is my tournament");
        tournament1.setChecksPayableTo("Payable to me");
        tournament1.setCity("San Diego, CA");
        tournament1.setTournamentId("123123123123");
        tournament1.setDirectorCell("671-123-1231");
        tournament1.setDirectorName("Mr. Director");
        tournament1.setDivisions(new String[] { "division 1", "division 2"});
        result.add(tournament1);

        ExcelFileSerializer serializer = new ExcelFileSerializer();
        serializer.serializeToFile(result);
    }

}
