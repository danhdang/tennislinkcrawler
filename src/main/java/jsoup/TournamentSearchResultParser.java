package jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import parsedresult.ParsedResult;
import parsedresult.ParsedTournament;

import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TournamentSearchResultParser {

    ParsedResult parsedResult = new ParsedResult();

    public ParsedResult parseSearchResult(String html) {
        Document document = Jsoup.parse(html);
        parseSearchResult(document);
        return parsedResult;
    }

    private void parseSearchResult(Document document) {

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
        Optional<TextNode> cityState = webElement.textNodes().stream().findFirst();
        if(cityState.isPresent()) {
            tournament.setCityState(cityState.get().text().trim());
        }

        Element mapElement = webElement.getElementsByTag("a").first();
        tournament.setGoogleMap(mapElement.attr("href").trim());

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

            Matcher levelMatcher2 = Pattern.compile("L(?<level>\\d+)\\s+.*", Pattern.CASE_INSENSITIVE).matcher(linkText);
            if(levelMatcher2.matches()) {
                tournament.setTournamentLevel(Integer.parseInt(levelMatcher2.group("level")));
            }


        }

        Element skillLevelElement = webElement.select(".tooltip2").first();
        if(skillLevelElement != null) {
            Optional<TextNode> skillLevelTextNode = skillLevelElement.textNodes().stream().findFirst();
            if(skillLevelTextNode.isPresent()) {
                tournament.setSkillLevel(skillLevelTextNode.get().text().trim());
            }
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
