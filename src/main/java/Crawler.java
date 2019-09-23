import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

public class Crawler {

    public void Run() {
        WebDriver driver = new JBrowserDriver();
        driver.get("https://tennislink.usta.com/tournaments/schedule/SearchResults.aspx?typeofsubmit=&Action=2&Keywords=&TournamentID=&SectionDistrict=&City=&State=&Zip=92129&Month=9&StartDate=&EndDate=&Day=&Year=2019&Division=&Category=&Surface=&OnlineEntry=&DrawsSheets=&UserTime=&Sanctioned=-1&AgeGroup=&SearchRadius=100");

       SearchResultParser parser = new SearchResultParser();
       parser.parseSearchResult(driver);


    }


}
