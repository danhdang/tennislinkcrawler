package jsoup;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsedresult.ParsedTournament;

import java.io.IOException;

public class TournamentPageParser {

    public void parsePage(String tournamentGoLinkId, ParsedTournament parsedTournament) {
        try {
            Document document = Jsoup.connect("https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=" + tournamentGoLinkId).get();
            parseDocument(document.outerHtml(), parsedTournament);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseDocument(String html, ParsedTournament parsedTournament) {
        Document document = Jsoup.parse(html);
        Element orgTable = document.getElementById("organization");
        Elements orgTableRows = orgTable.getElementsByTag("tr");

        parseOrgName(orgTableRows.get(1), parsedTournament);
        parseOrgPhone(orgTableRows.get(2), parsedTournament);
        parseOrgFax(orgTableRows.get(3), parsedTournament);
        parseOrgWebSite(orgTableRows.get(4), parsedTournament);
        parseOrgAddress(orgTableRows.get(5), parsedTournament);

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
