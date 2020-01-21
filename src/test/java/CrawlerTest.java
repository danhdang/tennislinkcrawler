import tennislink.crawler.parsers.TournamentPageParser;
import tennislink.crawler.parsers.TournamentSearchResultParser;
import tennislink.crawler.maps.CensusGeocoder;
import org.junit.Assert;
import org.junit.Test;
import tennislink.crawler.models.ParsedResult;
import tennislink.crawler.models.ParsedTournament;
import tennislink.crawler.parsers.SeleniumCrawler;
import tennislink.crawler.serializers.DynamoDbSerializer;
import tennislink.crawler.serializers.ExcelFileSerializer;
import tennislink.crawler.serializers.JsonSerializer;

import java.util.ArrayList;
import java.util.List;


public class CrawlerTest extends BaseTest {

    @Test
    public void TestCrawlSanDiego() {
        SeleniumCrawler crawler = new SeleniumCrawler();
        List<Integer> months = new ArrayList<>();
        months.add(1);
        months.add(2);
        ParsedResult parsedResult = crawler.findNearestTournaments("92129", months, 2020, 100);
        CensusGeocoder geocoder = new CensusGeocoder();
        parsedResult.getParsedTournamentList().forEach(t -> geocoder.geoCodeTournament(t));
        ExcelFileSerializer excelFileSerializer = new ExcelFileSerializer();
        excelFileSerializer.serializeToFile(parsedResult);
    }

    @Test
    public void TestRun() {
        SeleniumCrawler crawler = new SeleniumCrawler();

        List<String> states = new ArrayList<>();
        states.add("CA");

        List<Integer> months = new ArrayList<>();
        months.add(1);

        ParsedResult parsedResult = crawler.crawlRegion(states, months, 2020, 200);

        CensusGeocoder geocoder = new CensusGeocoder();
        parsedResult.getParsedTournamentList().forEach(t -> geocoder.geoCodeTournament(t));

//        DynamoDbSerializer serializer = new DynamoDbSerializer();
//        serializer.initializeTable();
//        serializer.serializeParsedResult(parsedResult);

        JsonSerializer serializer = new JsonSerializer();
        serializer.serializeToFiles(parsedResult);
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

}
