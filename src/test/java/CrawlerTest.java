import jsoup.TournamentCrawler;
import org.junit.Test;


public class CrawlerTest {

    @Test
    public void TestRun() {
        TournamentCrawler crawler = new TournamentCrawler();
        crawler.crawl();
    }

}
