package serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsedresult.ParsedResult;
import parsedresult.ParsedTournament;

import java.io.File;
import java.io.IOException;

public class JsonSerializer {
    ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(JsonSerializer.class);

    public String serialize(ParsedTournament parsedTournament) {
        try {
            return objectMapper.writeValueAsString(parsedTournament);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }
        return "";
    }

    public String serialize(ParsedResult result)  {
        try {
            return objectMapper.writeValueAsString(result.getParsedTournamentList());
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }

        return "";
    }

    public void saveToJsonFile(ParsedResult result) throws IOException {
        objectMapper.writeValue(new File("tournaments.json"), result.getParsedTournamentList());
    }


}
