package tennislink.crawler.parsers;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tennislink.crawler.models.PaginationInfo;
import tennislink.crawler.models.ParsedResult;

import java.util.List;

public class SeleniumCrawler {

    private static final Logger log = LoggerFactory.getLogger(SeleniumCrawler.class);

    public ParsedResult crawlRegion(List<String> states, List<Integer> months, Integer year, Integer searchRadius) {
        ParsedResult result = new ParsedResult();
        months.forEach(month -> {
            states.forEach(state -> {
                crawlRequestResults("", state, null, month, year, searchRadius, result);
            });
        });
        return result;
    }

    public void crawlRequestResults(String city, String state, Integer zipCode, Integer month, Integer year, Integer searchRadius, ParsedResult result) {
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
           parser.parseSearchResult(driver, result);

           if(paginationInfo.isLastPage())
               break;

           goToNextPage(paginationInfo, driver);
       }
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
        log.info("Navigated to: " + driver.getCurrentUrl());
    }
}
