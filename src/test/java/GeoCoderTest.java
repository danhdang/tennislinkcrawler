import tennislink.crawler.maps.CensusGeocoder;
import tennislink.crawler.models.GeocodeResponse;
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

    @Test
    public void testGeoCoderFail() {
        CensusGeocoder geocoder = new CensusGeocoder();
        GeocodeResponse response = geocoder.geocodeAddress("6336 Canoga Ave Box #1 Woodland Hills, CA 91367");

        Assert.assertFalse(response.getSuccess());

    }


}
