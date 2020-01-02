package tennislink.crawler.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ParsedResult {
    private ConcurrentHashMap<String, ParsedTournament> parsedTournamentHashMap = new ConcurrentHashMap<>();

    private static final Logger log = LoggerFactory.getLogger(ParsedResult.class);
    public ParsedResult() {
    }

    public ParsedResult(ParsedTournament tournament) {
        add(tournament);
    }

    public List<ParsedTournament> getParsedTournamentList() {
        return parsedTournamentHashMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

    public void merge(ParsedResult parsedResult) {
        parsedResult.getParsedTournamentList().forEach(tournament -> add(tournament));
    }

    public void add(ParsedTournament tournament) {
        if(parsedTournamentHashMap.containsKey(tournament.getTournamentId())) {
            log.info("ParsedResult already contains tournmanent: " + tournament.getTournamentId() + " " + tournament.getTournamentName());
        } else {
            log.info("ParsedResult adding tournment to hashmap: " + tournament.getTournamentId() + " " + tournament.getTournamentName());
            parsedTournamentHashMap.put(tournament.getTournamentId(), tournament);
        }
    }
}
