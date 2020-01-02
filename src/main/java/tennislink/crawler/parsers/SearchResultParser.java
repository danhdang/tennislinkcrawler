package tennislink.crawler.parsers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tennislink.crawler.models.PaginationInfo;
import tennislink.crawler.models.ParsedResult;

public class SearchResultParser {

    private TournamentPageParser pageParser = new TournamentPageParser();

    public PaginationInfo parsePagination(WebDriver webDriver) {
        WebElement pagelistWebElement = webDriver.findElement(By.className("pagelist"));
        PaginationParser parser = new PaginationParser();
        PaginationInfo paginationInfo = parser.parsePaginationHtml(pagelistWebElement.getAttribute("outerHTML"));
        return paginationInfo;
    }

    public void parseSearchResult(WebDriver driver, ParsedResult result) {
        TournamentSearchResultParser resultParser = new TournamentSearchResultParser();
        resultParser.parseSearchResult(driver.findElement(By.tagName("body")).getAttribute("outerHTML"), result);
        result.getParsedTournamentList().forEach(t -> pageParser.parsePage(t.getTournamentGoLinkId(), t));
    }
}
