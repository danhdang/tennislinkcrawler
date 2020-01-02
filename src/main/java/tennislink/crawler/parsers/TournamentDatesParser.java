package tennislink.crawler.parsers;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import tennislink.crawler.models.ParsedResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class DatesParser {

    public void parseDateFields(ParsedResult parsedResult) {
        parsedResult.getParsedTournamentList().forEach(parsedTournament -> {

            // Tournament Start Date
            if(StringUtils.isNotEmpty(parsedTournament.getTournamentStartDate())) {
                // looks like "01/04/2020"
                DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate startDate = LocalDate.from(f.parse(parsedTournament.getTournamentStartDate()));
                parsedTournament.setTournamentStartDate(startDate.toString());
            }

            // Entries closed
            if(StringUtils.isNotEmpty(parsedTournament.getEntriesClosed())) {

                Pattern dateWithTime = Pattern.compile("\\w+,\\s*\\w+\\s+\\d+,\\s*\\d{4}\\s+[\\d:]+\\s+\\w+\\s+[\\(\\w\\s\\)]+");
                Pattern dateWithoutTIme = Pattern.compile("\\w+,\\s*\\w+\\s+\\d+,\\s*\\d{4}\\s*$");
                if(dateWithTime.matcher(parsedTournament.getEntriesClosed()).find()) {
                    //looks like Tuesday, January 07, 2020 4:00 PM (Pacific Time)
                    DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy  hh:mm (zzz)");
                    LocalDate startDate = LocalDate.from(f.parse(parsedTournament.getEntriesClosed()));
                    parsedTournament.setTournamentStartDate(startDate.toString());

                } else if (dateWithoutTIme.matcher(parsedTournament.getEntriesClosed()).find()) {
                    //looks like "Tuesday, December 31, 2019"
                    DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
                    LocalDate startDate = LocalDate.from(f.parse(parsedTournament.getEntriesClosed()));
                    parsedTournament.setTournamentStartDate(startDate.toString());
                }
            }
        });
    }
}
