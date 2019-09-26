package selenium;

import jsoup.TournamentPageParser;
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

    ParsedResult parsedResult = new ParsedResult();
    TournamentPageParser pageParser = new TournamentPageParser();

    public void parsePagination(WebDriver webDriver) {
        WebElement pagelistWebElement = webDriver.findElement(By.className("pagelist"));
        PaginationParser parser = new PaginationParser();
        PaginationInfo paginationInfo = parser.parsePaginationHtml(pagelistWebElement.getAttribute("outerHTML"));

    }

    public ParsedResult parseSearchResult(WebDriver driver) {
        WebElement tournamentTable = driver.findElement(By.id("ctl00_mainContent_dgTournaments"));
        List<WebElement> rows = tournamentTable.findElements(By.xpath("tbody/tr"));
        rows.stream().forEach(r ->{
            if(isTournamentRow(r)) {
                ParsedTournament tournament = parseTournamentRow(r);
                pageParser.parsePage(tournament.getTournamentGoLinkId().toString(), tournament);
            }
        });
        return parsedResult;
    }

    private boolean isTournamentRow(WebElement rowWebElement) {
        return rowWebElement.getAttribute("align") == null;
    }

    private ParsedTournament parseTournamentRow(WebElement rowWebElement) {
        ParsedTournament tournament = new ParsedTournament();
        parsedResult.getParsedTournamentList().add(tournament);

        List<WebElement> tournamentCells = rowWebElement.findElements(By.tagName("td"));
        parseDateCell(tournament, tournamentCells.get(0));
        parseDescriptionCell(tournament, tournamentCells.get(1));
        parseLocationCell(tournament, tournamentCells.get(2));

        return tournament;
    }

    private void parseLocationCell(ParsedTournament tournament, WebElement webElement) {
    }

    private void parseDescriptionCell(ParsedTournament tournament, WebElement webElement) {
        WebElement tournamentLinkWebElement = webElement.findElement(By.tagName("a"));
        String linkHref = tournamentLinkWebElement.getAttribute("href").trim();
        Matcher goLinkMatcher = Pattern.compile(".*\\((?<id>\\d+)\\);",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ).matcher(linkHref);
        if(goLinkMatcher.matches()) {
            tournament.setTournamentGoLinkId(goLinkMatcher.group("id"));
        }

        String linkText = tournamentLinkWebElement.getText().trim();
        Matcher tournamentMatcher = Pattern.compile("(?<tournament>.*?)(\\s+-\\s+)(?<id>\\d{4,})$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ).matcher(linkText);
        if(tournamentMatcher.matches()) {
            tournament.setTournamentId(tournamentMatcher.group("id"));
            tournament.setTournamentName(tournamentMatcher.group("tournament"));

            Matcher levelMatcher = Pattern.compile(".*\\(Level\\s*(?<level>\\d+)\\s*\\).*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ).matcher(linkText);
            if(levelMatcher.matches()) {
                tournament.setTournamentLevel(Integer.parseInt(levelMatcher.group("level")));
            }
        }
    }

    private void parseDateCell(ParsedTournament tournament,  WebElement webElement) {
        tournament.setTournamentStartDate(webElement.getText().trim());
    }
}
