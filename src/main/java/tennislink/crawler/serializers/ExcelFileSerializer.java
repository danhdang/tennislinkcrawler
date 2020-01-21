package tennislink.crawler.serializers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tennislink.crawler.models.ParsedResult;
import tennislink.crawler.models.ParsedTournament;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

public class ExcelFileSerializer {

    private static final Logger log = LoggerFactory.getLogger(ExcelFileSerializer.class);

    public void serializeToFile(ParsedResult results) {
        // Create a workbook instances
        Workbook wb = new HSSFWorkbook();

        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("usta_", ".xls");
        } catch (IOException e) {
            log.error("Unable to create temporary file.");
        }

        log.info("Serializing to file " + tempFile.toString());

        OutputStream os = null;
        try {
            os = new FileOutputStream(tempFile.toString());
        } catch (FileNotFoundException ex) {
            log.error("Unable to open file: " + tempFile.toString() + ". " + ex.toString());
        }

        Sheet sheet = wb.createSheet("Tournaments");

        String[] headers = new String[]{"Name", "Date", "Location", "Level", "Link"};
        Row headerRow = sheet.createRow(0);
        IntStream.range(0, headers.length)
                .forEach(i -> {
                    String header = headers[i];
                    Cell headerCell = headerRow.createCell(i);
                    headerCell.setCellValue(header);
                });

        IntStream.range(0, results.getParsedTournamentList().size())
                .forEach(i -> {
                    ParsedTournament tournament = results.getParsedTournamentList().get(i);
                    Row tournamentRow = sheet.createRow(i + 1);

                    IntStream.range(0, headers.length)
                            .forEach(j -> {
                                String header = headers[j];
                                Cell tournamentCell = tournamentRow.createCell(j);
                                tournamentCell.setCellValue(getCellValue(tournament, header));
                            });
                });

        try {
            wb.write(os);
        } catch (IOException ex) {
            log.error("Error writing to file. " + ex.toString());
        }

    }

    private String getCellValue(ParsedTournament tournament, String header) {
        if (header.equalsIgnoreCase("Name")) {
            return tournament.getTournamentName();
        }
        if (header.equalsIgnoreCase("Date")) {
            return tournament.getTournamentStartDate();
        }
        if (header.equalsIgnoreCase("Location")) {
            return tournament.getOrganizationAddress();
        }
        if (header.equalsIgnoreCase("Level")) {
            if(tournament.getTournamentLevel() == null) {
                return "";
            }
            return tournament.getTournamentLevel().toString();
        }
        if (header.equalsIgnoreCase("Link")) {
            return tournament.getTournamentUstaUrl();
        }
        return "Missing Mapping";
    }

}
