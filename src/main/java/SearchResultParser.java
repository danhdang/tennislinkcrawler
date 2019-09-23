import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchResultParser {

    ParsedResult parsedResult = new ParsedResult();

    public void parseSearchResult(WebDriver driver) {

        WebElement tournamentTable = driver.findElement(By.id("ctl00_mainContent_dgTournaments"));
        List<WebElement> rows = tournamentTable.findElements(By.xpath("tbody/tr"));

        rows.stream().forEach(r ->{
            if(isTournamentRow(r)) {
                parseTournamentRow(r);
            }
        });

    }

    private boolean isTournamentRow(WebElement rowWebElement) {
        return rowWebElement.getAttribute("align") == null;
    }

    private void parseTournamentRow(WebElement rowWebElement) {
        ParsedTournament tournament = new ParsedTournament();
        parsedResult.getParsedTournamentList().add(tournament);

        List<WebElement> tournamentCells = rowWebElement.findElements(By.tagName("td"));
        parseDateCell(tournament, tournamentCells.get(0));
        parseDescriptionCell(tournament, tournamentCells.get(1));


    }

    private void parseDescriptionCell(ParsedTournament tournament, WebElement webElement) {
        WebElement tournamentLinkWebElement = webElement.findElement(By.tagName("a"));
        String linkText = tournamentLinkWebElement.getText();
        String

    }

    private void parseDateCell(ParsedTournament tournament,  WebElement webElement) {
        tournament.setTournamentStartDate(webElement.getText().trim());
    }
}
