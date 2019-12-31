import com.fasterxml.jackson.core.JsonProcessingException;
import jsoup.TournamentPageParser;
import jsoup.TournamentSearchResultParser;
import org.junit.Assert;
import org.junit.Test;
import parsedresult.ParsedResult;
import parsedresult.ParsedTournament;
import selenium.SeleniumCrawler;
import serializer.DynamoDbSerializer;
import serializer.JsonSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class CrawlerTest {

    @Test
    public void TestRun() throws IOException {
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
    }

    @Test
    public void testSearchResultParser() {
        TournamentSearchResultParser parser = new TournamentSearchResultParser();
        ParsedResult parsedResult = parser.parseSearchResult(getResourceFileAsString("searchresult1.html"));
        Assert.assertEquals(20, parsedResult.getParsedTournamentList().size());
    }



    public static String getResourceFileAsString(String fileName){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
