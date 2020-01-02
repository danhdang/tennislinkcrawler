import org.junit.Assert;
import org.junit.Test;
import tennislink.crawler.models.ParsedResult;
import tennislink.crawler.models.ParsedTournament;
import tennislink.crawler.parsers.TournamentDatesParser;

public class DateParserTest {

    @Test
    public void testSimpleDate() {
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
        tournament1.setTournamentStartDate("01/04/2020");
        tournament1.setEntriesClosed("Tuesday, December 31, 2019");
        result.add(tournament1);

        TournamentDatesParser parser = new TournamentDatesParser();
        parser.parseDateFields(result);

        Assert.assertEquals("2020-01-04T00:00:00", tournament1.getTournamentStartDate());
        Assert.assertEquals("2019-12-31T00:00:00", tournament1.getEntriesClosed());

        System.out.println(tournament1.getTournamentStartDate());
        System.out.println(tournament1.getEntriesClosed());

    }

    @Test
    public void testComplexDate() {
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
        tournament1.setTournamentStartDate("01/04/2020");
        tournament1.setEntriesClosed("Tuesday, January 07, 2020 4:00 PM (Pacific Time)");
        result.add(tournament1);

        TournamentDatesParser parser = new TournamentDatesParser();
        parser.parseDateFields(result);

        Assert.assertEquals("2020-01-04T00:00:00", tournament1.getTournamentStartDate());
        Assert.assertEquals("2020-01-07T16:00:00", tournament1.getEntriesClosed());

        System.out.println(tournament1.getTournamentStartDate());
        System.out.println(tournament1.getEntriesClosed());

    }
}
