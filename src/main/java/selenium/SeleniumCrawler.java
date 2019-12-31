package selenium;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pagination.PaginationInfo;
import parsedresult.ParsedResult;

public class SeleniumCrawler {

    private static final Logger log = LoggerFactory.getLogger(SeleniumCrawler.class);

    public ParsedResult Run() {
        return crawlRequestResults("", "", 92129, 1, 2020, 100);
    }

    public ParsedResult crawlRequestResults(String city, String state, Integer zipCode, Integer month, Integer year, Integer searchRadius) {
       WebDriver driver = new JBrowserDriver();

       String requestUrl = "https://tennislink.usta.com/tournaments/schedule/SearchResults.aspx" +
               "?typeofsubmit=" +
               "&Action=2" +
               "&Keywords=" +
               "&TournamentID=" +
               "&SectionDistrict=" +
               "&City=" + toString(city) +
               "&State=" + toString(state) +
               "&Zip=" + toString(zipCode) +
               "&Month=" + toString(month) +
               "&StartDate=" +
               "&EndDate=" +
               "&Day=" +
               "&Year=" + toString(year) +
               "&Division=" +
               "&Category=" +
               "&Surface=" +
               "&OnlineEntry=" +
               "&DrawsSheets=" +
               "&UserTime=" +
               "&Sanctioned=-1" +
               "&AgeGroup=" +
               "&SearchRadius=" + toString(searchRadius);

       log.info("Crawling " + requestUrl);

       driver.get(requestUrl);

       SearchResultParser parser = new SearchResultParser();
       while(true) {
           PaginationInfo paginationInfo = parser.parsePagination(driver);
           parser.parseSearchResult(driver);

           if(paginationInfo.isLastPage())
               break;

           goToNextPage(paginationInfo, driver);
       }
       return parser.getParsedResult();
    }

    private String toString(String value) {
        return value == null ? "" : value;
    }

    private String toString(Integer value) {
        return value == null ? "" : value.toString();
    }

    private void goToNextPage(PaginationInfo paginationInfo, WebDriver driver) {
        WebElement nextPage = driver.findElement(By.xpath("//*[@class=\"pagelist\"]//a[@href=\"" + paginationInfo.getNextPageHref() + "\"]"));
        nextPage.click();
    }
}
