package tennislink.crawler.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tennislink.crawler.models.ParsedResult;
import tennislink.crawler.models.ParsedTournament;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class JsonSerializer {

    ObjectMapper objectMapper = new ObjectMapper();
    private Properties appProps;
    private static final Logger log = LoggerFactory.getLogger(JsonSerializer.class);

    public JsonSerializer() {
        InputStream configResourceStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        appProps = new Properties();
        try {
            appProps.load(configResourceStream);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    public String serialize(ParsedTournament parsedTournament) {
        try {
            return objectMapper.writeValueAsString(parsedTournament);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }
        return "";
    }

    public void serializeToFiles(ParsedResult result)  {

        Path outputPath = null;
        try {
            outputPath = Files.createTempDirectory("tennislink");
        } catch (IOException e) {
            log.error("Error creating temp directory.");
        }

        log.info("Created temp directory: " + outputPath);

        Path finalOutputPath = outputPath;
        result.getParsedTournamentList().forEach(parsedTournament ->  {
            Path outputFilePath = Paths.get(finalOutputPath.toString(), parsedTournament.getTournamentId() + ".json");
            try {
                Files.deleteIfExists(outputFilePath);
                objectMapper.writeValue(new File(outputFilePath.toString()), parsedTournament);
            } catch (JsonProcessingException e) {
                log.error("Error while generating json file: " +e.toString());
            } catch (IOException e) {
                log.error("Error serializing file to " + outputFilePath.toString() + " Error: " + e.toString());
            }
        });

    }
}
