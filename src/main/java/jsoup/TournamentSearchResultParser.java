package jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsedresult.ParsedResult;
import parsedresult.ParsedTournament;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TournamentSearchResultParser {
    ParsedResult parsedResult = new ParsedResult();

    public void parseSearchResult(Document document) {

        Element tournamentTable = document.getElementById("ctl00_mainContent_dgTournaments");
        Elements rows = tournamentTable.select("tbody > tr");

        rows.stream().forEach(r ->{
            if(isTournamentRow(r)) {
                parseTournamentRow(r);
            }
        });

    }

    private boolean isTournamentRow(Element rowWebElement) {
        return !rowWebElement.hasAttr("align");
    }

    private void parseTournamentRow(Element rowWebElement) {
        ParsedTournament tournament = new ParsedTournament();
        parsedResult.getParsedTournamentList().add(tournament);

        List<Element> tournamentCells = rowWebElement.getElementsByTag("td");
        parseDateCell(tournament, tournamentCells.get(0));
        parseDescriptionCell(tournament, tournamentCells.get(1));
        parseLocationCell(tournament, tournamentCells.get(2));
    }

    private void parseLocationCell(ParsedTournament tournament, Element webElement) {
    }

    private void parseDescriptionCell(ParsedTournament tournament, Element webElement) {

        Element tournamentLinkWebElement = webElement.getElementsByTag("a").first();
        String linkHref = tournamentLinkWebElement.attr("href").trim();
        Matcher goLinkMatcher = Pattern.compile(".*\\((?<id>\\d+)\\);",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ).matcher(linkHref);
        if(goLinkMatcher.matches()) {
            tournament.setTournamentGoLinkId(goLinkMatcher.group("id"));
        }

        String linkText = tournamentLinkWebElement.text();
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

    private void parseDateCell(ParsedTournament tournament,  Element webElement) {
        tournament.setTournamentStartDate(webElement.text().trim());
    }
}
