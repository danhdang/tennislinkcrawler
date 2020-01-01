import maps.CensusGeocoder;
import maps.GeocodeResponse;
import org.junit.Assert;
import org.junit.Test;

public class GeoCoderTest extends BaseTest {


    @Test
    public void testGeoCoderRemote() {
        CensusGeocoder geocoder = new CensusGeocoder();
        GeocodeResponse response = geocoder.geocodeAddress("4600 Silver Hill Rd, SUITLAND, MD, 20746");

        Assert.assertEquals(response.getCity(), "SUITLAND");
        Assert.assertEquals(response.getState(), "MD");
        Assert.assertEquals(-76.92691, response.getCoordinatesX(), 0.01);
        Assert.assertEquals(38.846542, response.getCoordinatesY(), 0.01);
    }
}
