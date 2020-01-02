package tennislink.crawler.parsers;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tennislink.crawler.models.ParsedResult;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class TournamentDatesParser {

    private static final Logger log = LoggerFactory.getLogger(TournamentDatesParser.class);

    public void parseDateFields(ParsedResult parsedResult) {
        parsedResult.getParsedTournamentList().forEach(parsedTournament -> {

            // Tournament Start Date
            if(StringUtils.isNotEmpty(parsedTournament.getTournamentStartDate())) {
                // looks like "01/04/2020"
                DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate date = LocalDate.from(f.parse(parsedTournament.getTournamentStartDate()));
                parsedTournament.setTournamentStartDate(date.atStartOfDay().toInstant(ZoneOffset.UTC).toString());
            }

            // Entries closed
            if(StringUtils.isNotEmpty(parsedTournament.getEntriesClosed())) {

                Pattern dateWithTime = Pattern.compile("\\w+,\\s*\\w+\\s+\\d+,\\s*\\d{4}\\s+[\\d:]+\\s+\\w+\\s+[\\(\\w\\s\\)]+");
                Pattern dateWithoutTIme = Pattern.compile("\\w+,\\s*\\w+\\s+\\d+,\\s*\\d{4}\\s*$");
                if(dateWithTime.matcher(parsedTournament.getEntriesClosed()).find()) {
                    //looks like Tuesday, January 07, 2020 4:00 PM (Pacific Time)
                    DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy  h:mm a z");
                    String fixedTimeZone = fixTimeZone(parsedTournament.getEntriesClosed());
                    LocalDateTime date = LocalDateTime.from(f.parse(fixedTimeZone));
                    parsedTournament.setTournamentStartDate(date.toInstant(ZoneOffset.UTC).toString());

                } else if (dateWithoutTIme.matcher(parsedTournament.getEntriesClosed()).find()) {
                    //looks like "Tuesday, December 31, 2019"
                    DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
                    LocalDate date = LocalDate.from(f.parse(parsedTournament.getEntriesClosed()));
                    parsedTournament.setEntriesClosed(date.atStartOfDay().toInstant(ZoneOffset.UTC).toString());
                }
            }
        });
    }

    private String fixTimeZone(String dateString) {
        return dateString.replace("(Pacific Time)", "PST");
    }
}
