import org.junit.Assert;
import org.junit.Test;
import pagination.PaginationInfo;
import jsoup.PaginationParser;

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

    @Test
    public void testParsingLastPage() {
        String outterHTML = "<span class=\"pagelist\">Page:\n" +
                "            &lt;&lt; 1&nbsp; &gt;&gt;\n" +
                "        </span>";

        PaginationParser parser = new PaginationParser();
        PaginationInfo info = parser.parsePaginationHtml(outterHTML);

        Assert.assertEquals(1, info.getCurrentPage().intValue());
        Assert.assertEquals(null, info.getNextPage());
        Assert.assertEquals(null, info.getNextPageHref());
        Assert.assertTrue(info.isLastPage());
    }

    @Test
    public void testParsingLastPage2() {
        String outterHTML = "<span class=\"pagelist\">Page:\n" +
                "            <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl0','')\">&lt;&lt;</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl0','')\">1</a> 2&nbsp; &gt;&gt;\n" +
                "        </span>";

        PaginationParser parser = new PaginationParser();
        PaginationInfo info = parser.parsePaginationHtml(outterHTML);

        Assert.assertEquals(2, info.getCurrentPage().intValue());
        Assert.assertEquals(null, info.getNextPage());
        Assert.assertEquals(null, info.getNextPageHref());
        Assert.assertTrue(info.isLastPage());
    }

    @Test
    public void testParsingOtherPage() {
        String outterHTML = "<span class=\"pagelist\">Page:\n" +
                "            <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl8','')\">&lt;&lt;</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl0','')\">1</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl1','')\">2</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl2','')\">3</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl3','')\">4</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl4','')\">5</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl5','')\">6</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl6','')\">7</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl7','')\">8</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl8','')\">9</a> 10&nbsp;<a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl10','')\">11</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl11','')\">12</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl12','')\">13</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl13','')\">14</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl14','')\">15</a> ... <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl10','')\">&gt;&gt;</a>\n" +
                "        </span>";

        PaginationParser parser = new PaginationParser();
        PaginationInfo info = parser.parsePaginationHtml(outterHTML);

        Assert.assertEquals(10, info.getCurrentPage().intValue());
        Assert.assertEquals(11, info.getNextPage().intValue());
        Assert.assertEquals("javascript:__doPostBack('dgTournaments:_ctl1:_ctl10','')", info.getNextPageHref());
    }

    @Test
    public void testParsingEndOfGroup() {
        String outterHTML = "<span class=\"pagelist\">Page:\n" +
                "            <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl13','')\">&lt;&lt;</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl0','')\">1</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl1','')\">2</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl2','')\">3</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl3','')\">4</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl4','')\">5</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl5','')\">6</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl6','')\">7</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl7','')\">8</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl8','')\">9</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl9','')\">10</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl10','')\">11</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl11','')\">12</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl12','')\">13</a> <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl13','')\">14</a> 15&nbsp;... <a href=\"javascript:__doPostBack('dgTournaments:_ctl1:_ctl15','')\">&gt;&gt;</a>\n" +
                "        </span>";

        PaginationParser parser = new PaginationParser();
        PaginationInfo info = parser.parsePaginationHtml(outterHTML);

        Assert.assertEquals(15, info.getCurrentPage().intValue());
        Assert.assertEquals(null, info.getNextPage());
        Assert.assertEquals("javascript:__doPostBack('dgTournaments:_ctl1:_ctl15','')", info.getNextPageHref());
    }
}
