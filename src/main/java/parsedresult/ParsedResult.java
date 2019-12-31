package parsedresult;

import java.util.ArrayList;
import java.util.List;

public class ParsedResult {
    private List<ParsedTournament> parsedTournamentList = new ArrayList<ParsedTournament>();

    public ParsedResult() {
    }

    public ParsedResult(ParsedTournament tournament) {
        parsedTournamentList.add(tournament);
    }

    public List<ParsedTournament> getParsedTournamentList() {
        return parsedTournamentList;
    }

    public void setParsedTournamentList(List<ParsedTournament> parsedTournamentList) {
        this.parsedTournamentList = parsedTournamentList;
    }

}
