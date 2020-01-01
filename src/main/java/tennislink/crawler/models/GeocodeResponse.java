package tennislink.crawler.models;

public class GeocodeResponse {
    private String matchedAddress;
    private Double coordinatesX;
    private Double coordinatesY;
    private String fromAddress;
    private String toAddress;
    private String streetName;
    private String streetSuffixType;
    private String streetSuffixDirection;
    private String suffixQualifier;
    private String city;
    private String state;
    private String zipCode;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSuffixQualifier() {
        return suffixQualifier;
    }

    public void setSuffixQualifier(String suffixQualifier) {
        this.suffixQualifier = suffixQualifier;
    }

    public String getStreetSuffixDirection() {
        return streetSuffixDirection;
    }

    public void setStreetSuffixDirection(String streetSuffixDirection) {
        this.streetSuffixDirection = streetSuffixDirection;
    }

    public String getStreetSuffixType() {
        return streetSuffixType;
    }

    public void setStreetSuffixType(String streetSuffixType) {
        this.streetSuffixType = streetSuffixType;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public Double getCoordinatesY() {
        return coordinatesY;
    }

    public void setCoordinatesY(Double coordinatesY) {
        this.coordinatesY = coordinatesY;
    }

    public Double getCoordinatesX() {
        return coordinatesX;
    }

    public void setCoordinatesX(Double coordinatesX) {
        this.coordinatesX = coordinatesX;
    }

    public String getMatchedAddress() {
        return matchedAddress;
    }

    public void setMatchedAddress(String matchedAddress) {
        this.matchedAddress = matchedAddress;
    }
}
