package selenium;

import jsoup.TournamentPageParser;
import jsoup.TournamentSearchResultParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pagination.PaginationInfo;
import jsoup.PaginationParser;
import parsedresult.ParsedResult;
import parsedresult.ParsedTournament;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchResultParser {

    private ParsedResult parsedResult = new ParsedResult();
    private TournamentPageParser pageParser = new TournamentPageParser();

    public ParsedResult getParsedResult() {
        return parsedResult;
    }

    public PaginationInfo parsePagination(WebDriver webDriver) {
        WebElement pagelistWebElement = webDriver.findElement(By.className("pagelist"));
        PaginationParser parser = new PaginationParser();
        PaginationInfo paginationInfo = parser.parsePaginationHtml(pagelistWebElement.getAttribute("outerHTML"));
        return paginationInfo;
    }

    public ParsedResult parseSearchResult(WebDriver driver) {
        TournamentSearchResultParser resultParser = new TournamentSearchResultParser();
        ParsedResult result = resultParser.parseSearchResult(driver.findElement(By.tagName("body")).getAttribute("outerHTML"));
        result.getParsedTournamentList().forEach(t -> pageParser.parsePage(t.getTournamentGoLinkId(), t));
        parsedResult.getParsedTournamentList().addAll(result.getParsedTournamentList());
        return parsedResult;
    }
}
