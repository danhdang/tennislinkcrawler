package tennislink.crawler.parsers;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tennislink.crawler.models.ParsedResult;
import tennislink.crawler.models.ParsedTournament;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class TournamentDatesParser {

    private static final Logger log = LoggerFactory.getLogger(TournamentDatesParser.class);

    public void parseDateFields(ParsedResult parsedResult) {
        parsedResult.getParsedTournamentList().forEach(this::parseDateFields);
    }

    private String getLocalDateTimeString(LocalDateTime atTime) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(atTime);
    }

    private String fixTimeZone(String dateString) {
        return dateString.replace("(Pacific Time)", "PST").trim();
    }

    private void parseDateFields(ParsedTournament parsedTournament) {
        if (StringUtils.isNotEmpty(parsedTournament.getTournamentStartDate())) {
            // looks like "01/04/2020"
            try {
                DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate date = LocalDate.from(f.parse(parsedTournament.getTournamentStartDate()));
                parsedTournament.setTournamentStartDate(getLocalDateTimeString(date.atTime(0, 0)));
            } catch (DateTimeParseException ex) {
                log.error("Unable to parse tournament start date string. " + ex.toString());
            }
        }

        // Entries closed
        if (StringUtils.isNotEmpty(parsedTournament.getEntriesClosed())) {

            Pattern dateWithTime = Pattern.compile("\\w+,\\s*\\w+\\s+\\d+,\\s*\\d{4}\\s+[\\d:]+\\s+\\w+\\s+[\\(\\w\\s\\)]+");
            Pattern dateWithoutTIme = Pattern.compile("\\w+,\\s*\\w+\\s+\\d+,\\s*\\d{4}\\s*$");
            try {
                if (dateWithTime.matcher(parsedTournament.getEntriesClosed()).find()) {
                    //looks like Tuesday, January 07, 2020 4:00 PM (Pacific Time)
                    DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy h:m a z");
                    String fixedTimeZone = fixTimeZone(parsedTournament.getEntriesClosed());
                    LocalDateTime date = LocalDateTime.from(f.parse(fixedTimeZone));
                    parsedTournament.setEntriesClosed(getLocalDateTimeString(date));

                } else if (dateWithoutTIme.matcher(parsedTournament.getEntriesClosed()).find()) {
                    //looks like "Tuesday, December 31, 2019"
                    DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
                    LocalDate date = LocalDate.from(f.parse(parsedTournament.getEntriesClosed()));
                    parsedTournament.setEntriesClosed(getLocalDateTimeString(date.atStartOfDay()));
                }
            } catch (DateTimeParseException ex) {
                log.error("Unable to parse entries closed date string. Original string: " + parsedTournament.getEntriesClosed() + " Exception: " + ex.toString());
            }
        }
    }
}
