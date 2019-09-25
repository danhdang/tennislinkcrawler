import org.junit.Assert;
import org.junit.Test;
import pagination.PaginationInfo;
import pagination.PaginationParser;

public class PaginationTest {


    @Test
    public void testParsing() {
        String outterHTML = "<span class=\"pagelist\">Page:\n" +
                "            &lt;&lt; 1&nbsp;<a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl1','')\">2</a>  <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl1' ,'')\">&gt;&gt;</a>\n" +
                "        </span>";

        PaginationParser parser = new PaginationParser();
        PaginationInfo info = parser.parsePaginationHtml(outterHTML);

        Assert.assertEquals(1, info.getCurrentPage().intValue());
        Assert.assertEquals(2, info.getNextPage().intValue());
        Assert.assertEquals("javascript:__doPostBack('dgTournaments:_ctl1:_ctl1','')", info.getNextPageHref());
    }
}
