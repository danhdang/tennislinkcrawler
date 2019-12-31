package jsoup;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsedresult.ParsedTournament;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TournamentPageParser {

    private static final Logger log = LoggerFactory.getLogger(TournamentPageParser.class);

    public void parsePage(String tournamentGoLinkId, ParsedTournament parsedTournament) {
        try {
            String url = "https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=" + tournamentGoLinkId;
            log.info("Parsing tournament page: " + url);
            Document document = Jsoup.connect(url).get();
            parseDocument(document.outerHtml(), parsedTournament);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseDocument(String html, ParsedTournament parsedTournament) {
        Document document = Jsoup.parse(html);

        Element heading = document.getElementsByTag("h1").first();
        parsedTournament.setTournamentName(heading.text().trim());

        Element orgTable = document.getElementById("organization");
        Elements orgTableRows = orgTable.getElementsByTag("tr");

        parseOrgName(orgTableRows.get(1), parsedTournament);
        parseOrgPhone(orgTableRows.get(2), parsedTournament);
        parseOrgFax(orgTableRows.get(3), parsedTournament);
        parseOrgWebSite(orgTableRows.get(4), parsedTournament);
        parseOrgAddress(orgTableRows.get(5), parsedTournament);
        parseGoogleMapLink(orgTableRows.get(5), parsedTournament);

        Element contactsTable = document.getElementById("contact");
        Elements contactsTableRows = contactsTable.getElementsByTag("tr");

        parseDirectorName(contactsTableRows.get(1), parsedTournament);
        parseDirectorPhone(contactsTableRows.get(2), parsedTournament);
        parseDirectorCell(contactsTableRows.get(3), parsedTournament);
        parseDirectorFax(contactsTableRows.get(4), parsedTournament);
        parseDirectorEmail(contactsTableRows.get(5), parsedTournament);
        parseRefereeName(contactsTableRows.get(6), parsedTournament);
        parseRefereePhone(contactsTableRows.get(7), parsedTournament);
        parseRefereeEmail(contactsTableRows.get(8), parsedTournament);

        Element entriesTable = document.getElementById("entry_info");
        Elements entryTableRows = entriesTable.getElementsByTag("tr");

        parseEntriesClosed(entryTableRows.get(1), parsedTournament);
        parseEntryInformation(entryTableRows.get(2), parsedTournament);
        parseChecksPayableTo(entryTableRows.get(3), parsedTournament);
        parseSendChecksTo(entryTableRows.get(4), parsedTournament);
        parseTournamentWebsite(entryTableRows.get(5), parsedTournament);

        Element tournamentInfoTable = document.getElementsByClass("tournament_info").first();
        Elements tournamentInfoRows = tournamentInfoTable.getElementsByTag("tr");

        parseTournamentId(tournamentInfoRows.get(1), parsedTournament);
        parseSkillLevel(tournamentInfoRows.get(1), parsedTournament);
        parseTournamentDateRange(tournamentInfoRows.get(1), parsedTournament);

        Element importantInfoElement = document.getElementById("important_info");
        parsedTournament.setImportantInfoText(importantInfoElement.text());
        parsedTournament.setImportantInfoHtml(importantInfoElement.html());

    }

    private void parseTournamentId(Element element, ParsedTournament parsedTournament) {
        String tdText = element.getElementsByTag("td").get(0).text();
        Matcher matcher = Pattern.compile("(?<id>\\d{4,})", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(tdText);
        if(matcher.find()) {
            parsedTournament.setTournamentId(matcher.group("id"));
        }
    }

    private void parseTournamentDateRange(Element element, ParsedTournament parsedTournament) {
        String tdText = element.getElementsByTag("td").get(1).text();
        parsedTournament.setTournamentDates(tdText.trim());
    }

    private void parseSkillLevel(Element element, ParsedTournament parsedTournament) {
        String tdText = element.getElementsByTag("td").get(0).text();

        List<String> skillLevels = new ArrayList<>();
        if(tdText.toLowerCase().contains("entry level")) {
            skillLevels.add("Entry Level");
        }

        if(tdText.toLowerCase().contains("intermediate")) {
            skillLevels.add("Intermediate");
        }

        if(tdText.toLowerCase().contains("advanced")) {
            skillLevels.add("Advanced");
        }

        parsedTournament.setSkillLevel(skillLevels.toArray(new String[0]));
    }

    private void parseGoogleMapLink(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setGoogleMap(element.getElementById("ctl00_mainContent_lnkMap").attr("href"));
    }

    private void parseTournamentWebsite(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setTournamentWebsite(getSecondCellText(element));
    }

    private void parseSendChecksTo(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setSendChecksTo(getSecondCellText(element));
    }

    private void parseChecksPayableTo(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setChecksPayableTo(getSecondCellText(element));
    }

    private void parseEntryInformation(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setEntriesInformation(getSecondCellText(element));
    }

    private void parseEntriesClosed(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setEntriesClosed(getSecondCellText(element));
    }

    private void parseRefereeEmail(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setRefereeEmail(getSecondCellText(element));
    }

    private void parseRefereePhone(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setRefereePhone(getSecondCellText(element));
    }

    private void parseRefereeName(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setRefereeName(getSecondCellText(element));
    }

    private void parseDirectorEmail(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setDirectorEmail(getSecondCellText(element));
    }

    private void parseDirectorFax(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setDirectorFax(getSecondCellText(element));
    }

    private void parseDirectorCell(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setDirectorCell(getSecondCellText(element));
    }

    private void parseDirectorPhone(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setDirectorPhone(getSecondCellText(element));
    }

    private void parseDirectorName(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setDirectorName(getSecondCellText(element));
    }

    private String getSecondCellText(Element element) {
        return element.getElementsByTag("td").get(1).text().trim();
    }

    private void parseOrgWebSite(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setOrganizationWebsite(getSecondCellText(element));
    }

    private void parseOrgAddress(Element element, ParsedTournament parsedTournament) {
        String elemText = getSecondCellText(element);
        String address = StringUtils.removeEnd(elemText, "Map").trim();
        parsedTournament.setOrganizationAddress(StringUtils.trim(address));
    }

    private void parseOrgFax(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setOrganizationFax(getSecondCellText(element));
    }

    private void parseOrgPhone(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setOrganizationPhone(getSecondCellText(element));
    }

    private void parseOrgName(Element element, ParsedTournament parsedTournament) {
        parsedTournament.setOrganizationName(getSecondCellText(element));
    }
}
