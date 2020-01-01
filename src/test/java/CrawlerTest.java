import jsoup.TournamentPageParser;
import jsoup.TournamentSearchResultParser;
import org.junit.Assert;
import org.junit.Test;
import parsedresult.ParsedResult;
import parsedresult.ParsedTournament;
import selenium.SeleniumCrawler;
import serializer.DynamoDbSerializer;


public class CrawlerTest extends BaseTest {

    @Test
    public void TestRun() {
        SeleniumCrawler crawler = new SeleniumCrawler();
        ParsedResult parsedResult = crawler.Run();
        DynamoDbSerializer serializer = new DynamoDbSerializer();
        serializer.initializeTable();
        serializer.serializeParsedResult(parsedResult);
    }

    @Test
    public void TestTournamentPageParser() {
        TournamentPageParser parser = new TournamentPageParser();
        ParsedTournament tournament = new ParsedTournament();

        parser.parseDocument(getResourceFileAsString("tournamentPage1.html"), tournament);

        Assert.assertEquals("Orange County Tennis Academy/Anaheim Tennis Center", tournament.getOrganizationName());

        DynamoDbSerializer serializer = new DynamoDbSerializer();
        serializer.serializeParsedResult(new ParsedResult(tournament));
    }

    @Test
    public void TestTournamentPageParserHttp() {
        TournamentPageParser parser = new TournamentPageParser();
        ParsedTournament tournament = new ParsedTournament();

        parser.parsePage("251658", tournament);

        DynamoDbSerializer serializer = new DynamoDbSerializer();
        serializer.serializeParsedResult(new ParsedResult(tournament));
    }

    @Test
    public void testSearchResultParser() {
        TournamentSearchResultParser parser = new TournamentSearchResultParser();
        ParsedResult parsedResult = parser.parseSearchResult(getResourceFileAsString("searchresult1.html"));
        Assert.assertEquals(20, parsedResult.getParsedTournamentList().size());
    }


}
