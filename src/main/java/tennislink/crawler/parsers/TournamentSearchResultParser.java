package tennislink.crawler.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tennislink.crawler.models.ParsedResult;
import tennislink.crawler.models.ParsedTournament;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TournamentSearchResultParser {

    private static final Logger log = LoggerFactory.getLogger(TournamentSearchResultParser.class);

    public void parseSearchResult(String html, ParsedResult result) {
        Document document = Jsoup.parse(html);
        parseSearchResult(document, result);
    }

    private void parseSearchResult(Document document, ParsedResult result) {

        Element tournamentTable = document.getElementById("ctl00_mainContent_dgTournaments");
        Elements rows = tournamentTable.select("tbody > tr");

        if(rows == null) {
            return;
        }

        rows.stream().forEach(r ->{
            if(isTournamentRow(r)) {
                parseTournamentRow(r, result);
            }
        });

    }

    private boolean isTournamentRow(Element rowWebElement) {
        return !rowWebElement.hasAttr("align");
    }

    private void parseTournamentRow(Element rowWebElement, ParsedResult result) {
        ParsedTournament tournament = new ParsedTournament();
        List<Element> tournamentCells = rowWebElement.getElementsByTag("td");
        parseDateCell(tournament, tournamentCells.get(0));
        parseDescriptionCell(tournament, tournamentCells.get(1));
        parseLocationCell(tournament, tournamentCells.get(2));

        result.add(tournament);
    }

    private void parseLocationCell(ParsedTournament tournament, Element webElement) {
        Optional<TextNode> cityState = webElement.textNodes().stream().findFirst();
        if(cityState.isPresent() && cityState.get().text().contains(",")) {
            String[] cityStateParts = cityState.get().text().split(",");
            tournament.setCity(cityStateParts[0].trim());
            tournament.setState(cityStateParts[1].trim());
        }
    }

    private void parseDescriptionCell(ParsedTournament tournament, Element webElement) {

        Element tournamentLinkWebElement = webElement.getElementsByTag("a").first();
        String linkHref = tournamentLinkWebElement.attr("href").trim();
        Matcher goLinkMatcher = Pattern.compile(".*\\((?<id>\\d+)\\);",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ).matcher(linkHref);
        if(goLinkMatcher.matches()) {
            tournament.setTournamentGoLinkId(goLinkMatcher.group("id"));
            tournament.setTournamentUstaUrl("https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=" + tournament.getTournamentGoLinkId());
        }

        String linkText = tournamentLinkWebElement.text();
        Matcher tournamentMatcher = Pattern.compile("(?<tournament>.*?)(\\s+-\\s+)(?<id>\\d{4,})$", Pattern.CASE_INSENSITIVE).matcher(linkText);
        if(tournamentMatcher.matches()) {
            tournament.setTournamentId(tournamentMatcher.group("id"));
            tournament.setTournamentName(tournamentMatcher.group("tournament"));

            Matcher levelMatcher = Pattern.compile(".*\\(Level\\s*(?<level>\\d+)\\s*\\).*", Pattern.CASE_INSENSITIVE).matcher(linkText);
            if(levelMatcher.matches()) {
                tournament.setTournamentLevel(Integer.parseInt(levelMatcher.group("level")));
            }
        }

        Matcher levelMatcher2 = Pattern.compile("L(?<level>\\d+)\\s+.*", Pattern.CASE_INSENSITIVE).matcher(linkText);
        if(levelMatcher2.matches()) {
            tournament.setTournamentLevel(Integer.parseInt(levelMatcher2.group("level")));
        }

        Elements divisionElements = webElement.select("ul.plain-list.compact");
        String[] divisions = divisionElements.stream().map(d -> cleanDivisionText(d.text())).toArray(String[]::new);
        tournament.setDivisions(divisions);

    }

    private String cleanDivisionText(String text) {
        return text.replace("(NEFNon-Elimination Format A non-elimination format is a type of draw in which players will play multiple matches regardless of their results in those matches. Non-Elimination Formats will be played as Round Robin or Compass Draws based upon the number of participants in the event.)", "").trim();
    }

    private void parseDateCell(ParsedTournament tournament,  Element webElement) {
        tournament.setTournamentStartDate(webElement.text().trim());
    }
}
