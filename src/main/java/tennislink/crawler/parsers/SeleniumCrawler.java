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

    public ParsedResult findNearestTournaments(String zipcode, List<Integer> months,  Integer year, QuickSearch quickSearch, TournamentDivision division, Integer searchRadius) {
        ParsedResult result = new ParsedResult();
        months.forEach(month -> {
            crawlRequestResults("", "", zipcode, month, year, quickSearch, division, null, searchRadius, result);
        });
        return result;
    }

    public ParsedResult crawlRegion(List<String> states, List<Integer> months, Integer year, Integer searchRadius) {
        ParsedResult result = new ParsedResult();
        months.forEach(month -> {
            states.forEach(state -> {
                crawlRequestResults("", state, null, month, year, null, null,null, searchRadius, result);
            });
        });
        return result;
    }

    public void crawlRequestResults(String city, String state, String zipCode, Integer month, Integer year, QuickSearch quickSearch, TournamentDivision division, Integer category, Integer searchRadius, ParsedResult result) {
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
               "&Division=" + toString(division) +
               "&Category=" + toString(category) +
               "&Surface=" +
               "&OnlineEntry=" +
               "&DrawsSheets=" +
               "&UserTime=" +
               "&Sanctioned=-1" +
               "&AgeGroup=" +
               "&SearchRadius=" + toString(searchRadius) +
               "&QuickSearch=" + toString(quickSearch);

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

    private String toString(TournamentDivision division) { return division == null ? "" : division.getValue(); }

    private String toString(QuickSearch quickSearch) { return quickSearch == null ? "" : quickSearch.getValue().toString(); }

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
