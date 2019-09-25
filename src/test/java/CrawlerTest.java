import jsoup.JsoupCrawler;
import org.junit.Test;


public class CrawlerTest {

    @Test
    public void TestRun() {
        JsoupCrawler crawler = new JsoupCrawler();
        crawler.crawl();
    }

}
