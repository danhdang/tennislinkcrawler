import jsoup.TournamentPageParser;
import org.junit.Assert;
import org.junit.Test;
import parsedresult.ParsedResult;
import parsedresult.ParsedTournament;
import selenium.SeleniumCrawler;


public class CrawlerTest {

    @Test
    public void TestRun() {
        SeleniumCrawler crawler = new SeleniumCrawler();
        ParsedResult parsedResult = crawler.Run();
    }

    @Test
    public void TestTournamentPageParser() {
        TournamentPageParser parser = new TournamentPageParser();
        ParsedTournament tournament = new ParsedTournament();

        parser.parsePage("233440", tournament);

        Assert.assertEquals("Orange County Tennis Academy/Anaheim Tennis Center", tournament.getOrganizationName());
    }

}
