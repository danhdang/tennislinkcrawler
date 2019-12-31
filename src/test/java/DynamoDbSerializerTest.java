import org.junit.Test;
import parsedresult.ParsedResult;
import parsedresult.ParsedTournament;
import serializer.DynamoDbSerializer;

import java.util.ArrayList;
import java.util.List;


public class DynamoDbSerializerTest {

    @Test
    public void testSerializer() {
        ParsedResult result = new ParsedResult();

        List<ParsedTournament> tournaments = new ArrayList<>();

        ParsedTournament tournament1 = new ParsedTournament();
        tournament1.setTournamentId("123123123123");
        tournament1.setTournamentName("This is my tournament");
        tournament1.setChecksPayableTo("Payable to me");
        tournament1.setCityState("San Diego, CA");
        tournament1.setTournamentId("123123123123");
        tournament1.setDirectorCell("671-123-1231");
        tournament1.setDirectorName("Mr. Director");
        tournaments.add(tournament1);

        result.setParsedTournamentList(tournaments);

        DynamoDbSerializer serializer = new DynamoDbSerializer();
        serializer.initializeTable();
        serializer.serializeParsedResult(result);
    }

}
