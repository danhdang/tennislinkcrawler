package tennislink.crawler.maps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tennislink.crawler.models.ParsedTournament;

import java.io.IOException;

public class CensusGeocoder {

    OkHttpClient client = new OkHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(CensusGeocoder.class);

    public void geoCodeTournament(ParsedTournament tournament) {
        GeocodeResponse response = geocodeAddress(tournament.getOrganizationAddress());

        if(response == null) {
            return;
        }

        tournament.setLocationLatituate(response.getCoordinatesX());
        tournament.setLocationLongitude(response.getCoordinatesY());
    }

    public GeocodeResponse geocodeAddress(String address) {

        log.info("CensusGeocoder geocoding address: " +address);
        String requestUrl = "https://geocoding.geo.census.gov/geocoder/locations/onelineaddress?address" +
                "=" + address +
                "&benchmark=9" +
                "&format=json";

        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return parseResponse(response.body().string());
        } catch (IOException e) {
            log.error(e.toString());
        }
        return null;
    }

    private GeocodeResponse parseResponse(String responseBody) {

        log.info("CensusGeocoder parsing response: " + responseBody);

        JsonNode json;
        try {
            json = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            log.error("CensusCoder parse error: " + e.toString());
            return null;
        }

        GeocodeResponse response = new GeocodeResponse();
        response.setCity(json.at("/result/addressMatches/0/addressComponents/city").asText());
        response.setState(json.at("/result/addressMatches/0/addressComponents/state").asText());
        response.setZipCode(json.at("/result/addressMatches/0/addressComponents/zip").asText());
        response.setState(json.at("/result/addressMatches/0/addressComponents/state").asText());
        response.setCoordinatesX(json.at("/result/addressMatches/0/coordinates/x").asDouble());
        response.setCoordinatesY(json.at("/result/addressMatches/0/coordinates/y").asDouble());
        response.setFromAddress(json.at("/result/addressMatches/0/addressComponents/fromAddress").asText());
        response.setStreetName(json.at("/result/addressMatches/0/addressComponents/streetName").asText());
        response.setStreetSuffixDirection(json.at("/result/addressMatches/0/addressComponents/suffixDirection").asText());
        response.setStreetSuffixType(json.at("/result/addressMatches/0/addressComponents/suffixType").asText());
        response.setSuffixQualifier(json.at("/result/addressMatches/0/addressComponents/suffixQualifier").asText());
        response.setToAddress(json.at("/result/addressMatches/0/addressComponents/toAddress").asText());
        response.setMatchedAddress(json.at("/result/addressMatches/0/matchedAddress").asText());

        return response;

    }
}
