package tennislink.crawler.models;

import org.apache.commons.lang.StringUtils;

public class PaginationInfo {
    private Integer currentPage;
    private Integer nextPage;
    private String nextPageHref;
    private boolean isLastPage;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public String getNextPageHref() {
        return nextPageHref;
    }

    public void setNextPageHref(String nextPageHref) {
        this.nextPageHref = nextPageHref;
    }

    public boolean isLastPage() {
        return StringUtils.isBlank(nextPageHref);
    }
}
