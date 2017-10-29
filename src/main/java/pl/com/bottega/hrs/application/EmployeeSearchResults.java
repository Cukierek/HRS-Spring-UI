package pl.com.bottega.hrs.application;

import java.util.List;

public class EmployeeSearchResults {

    private List<BasicEmployeeDto> results;

    private int totalCount;

    private int pageSize;

    private int pageNumber;
    private int pagesCount;

    public List<BasicEmployeeDto> getResults() {
        return results;
    }

    public void setResults(List<BasicEmployeeDto> results) {
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }
}
