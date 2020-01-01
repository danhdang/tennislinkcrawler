import org.junit.Test;
import tennislink.crawler.models.ParsedResult;
import tennislink.crawler.models.ParsedTournament;
import tennislink.crawler.serializers.DynamoDbSerializer;

import java.util.ArrayList;
import java.util.List;


public class DynamoDbSerializerTest extends BaseTest {

    @Test
    public void testSerializer() {
        ParsedResult result = new ParsedResult();

        List<ParsedTournament> tournaments = new ArrayList<>();

        ParsedTournament tournament1 = new ParsedTournament();
        tournament1.setTournamentId("123123123123");
        tournament1.setTournamentName("This is my tournament");
        tournament1.setChecksPayableTo("Payable to me");
        tournament1.setCity("San Diego, CA");
        tournament1.setTournamentId("123123123123");
        tournament1.setDirectorCell("671-123-1231");
        tournament1.setDirectorName("Mr. Director");
        tournament1.setDivisions(new String[] { "division 1", "division 2"});
        tournaments.add(tournament1);

        result.setParsedTournamentList(tournaments);

        DynamoDbSerializer serializer = new DynamoDbSerializer();
        serializer.serializeParsedResult(result);
    }

}
