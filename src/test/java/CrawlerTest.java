import tennislink.crawler.parsers.TournamentPageParser;
import tennislink.crawler.parsers.TournamentSearchResultParser;
import tennislink.crawler.maps.CensusGeocoder;
import org.junit.Assert;
import org.junit.Test;
import tennislink.crawler.models.ParsedResult;
import tennislink.crawler.models.ParsedTournament;
import tennislink.crawler.parsers.SeleniumCrawler;
import tennislink.crawler.serializers.DynamoDbSerializer;


public class CrawlerTest extends BaseTest {

    @Test
    public void TestRun() {
        SeleniumCrawler crawler = new SeleniumCrawler();
        ParsedResult parsedResult = crawler.Run();

        CensusGeocoder geocoder = new CensusGeocoder();
        parsedResult.getParsedTournamentList().forEach(t -> geocoder.geoCodeTournament(t));

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
