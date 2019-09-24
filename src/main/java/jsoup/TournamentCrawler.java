package jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;

public class TournamentCrawler {

    public void crawl() {

        Document document = null;
        try {
            document = Jsoup.connect("https://tennislink.usta.com/tournaments/schedule/SearchResults.aspx?typeofsubmit=&Action=2&Keywords=&TournamentID=&SectionDistrict=&City=&State=&Zip=92129&Month=9&StartDate=&EndDate=&Day=&Year=2019&Division=&Category=&Surface=&OnlineEntry=&DrawsSheets=&UserTime=&Sanctioned=-1&AgeGroup=&SearchRadius=100").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TournamentSearchResultParser parser = new TournamentSearchResultParser();
        parser.parseSearchResult(document);
    }
}
