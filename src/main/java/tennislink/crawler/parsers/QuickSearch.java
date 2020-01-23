package tennislink.crawler.parsers;

public enum QuickSearch {
    CurrentlyRegisteringOnline(3),
    CurrentlyRegistering(7),
    InProgress(4),
    JustCompleted(5),
    AllUpcoming(6),
    NationalAdultTournaments(8),
    NationalJuniorTournaments(9),
    USOpenNationalPlayoffs(10),
    USTAProCircuitEvents(11);


    private int value;

    QuickSearch(int val) {
        value = val;
    }

    public Integer getValue() {
        return value;
    }
}
