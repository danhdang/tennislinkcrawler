package pagination;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PaginationParser {

    public 

    public PaginationInfo parsePaginationHtml(String html) {

        PaginationInfo info = new PaginationInfo();

        Document document = Jsoup.parse(html);
        Elements paginationLinks = document.getElementsByTag("a");

        paginationLinks.stream().map(p -> {
            p.text()
        })

        return info;
    }
}
