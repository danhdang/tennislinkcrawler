package jsoup;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.IntegerValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import pagination.PaginationInfo;
import sun.security.ssl.Debug;

import java.util.Optional;
import java.util.regex.Pattern;

public class PaginationParser {


    public PaginationInfo parsePaginationHtml(String html) {

        PaginationInfo info = new PaginationInfo();

        Document document = Jsoup.parse(html);


        Optional<TextNode> paginationText =  document.getElementsByClass("pagelist")
                .first()
                .textNodes()
                .stream()
                .filter(t -> Pattern.matches(".*\\d.*", t.text()))
                .findFirst();

        if(!paginationText.isPresent()) {
            return info;
        }

        String text = paginationText.get().text();
        text = text.replace("Page", "")
                .replace("<<", "")
                .replace(">>", "")
                .replace(":", "")
                .replace(".", "")
                .replace('\u00A0', ' ')
                .trim();
        info.setCurrentPage(IntegerValidator.getInstance().validate(text));

        Integer nextPage = info.getCurrentPage() + 1;
        Optional<Element> nextPageElement = document.getElementsByClass("pagelist")
                .first()
                .getElementsByTag("a")
                .stream()
                .filter(a -> a.text().trim().equalsIgnoreCase(nextPage.toString()))
                .findFirst();

        if(nextPageElement.isPresent()) {
          info.setNextPage(nextPage);
          info.setNextPageHref(nextPageElement.get().attr("href"));
        } else {
            Optional<Element> nextGroup = document.getElementsByClass("pagelist")
                    .first()
                    .getElementsByTag("a")
                    .stream()
                    .filter(a -> a.text().trim().equalsIgnoreCase(">>"))
                    .findFirst();

            if(nextGroup.isPresent()) {
                info.getNextPage();
                info.setNextPageHref(nextGroup.get().attr("href"));
            }
        }
        return info;
   }
}
