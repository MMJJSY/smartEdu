package com.application.smartEdu.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PageMaker {
    private int page = 1;
    private int perPageNum = 8;
    private String searchType = "";
    private String keyword = "";
    private String statusFilter = ""; // 추가
    private String categoryFilter = ""; // 추가

    private int totalCount;
    private int startPage = 1;
    private int endPage = 1;
    private int realEndPage;
    private boolean prev;
    private boolean next;
    private String orderBy = ""; // 기본값 설정

    private int displayPageNum = 4;

    public int getStartRow() {
        return (this.page - 1) * this.perPageNum;
    }

    private void calcData() {
        endPage = (int) (Math.ceil(page / (double) displayPageNum)
                * displayPageNum);

        startPage = (endPage - displayPageNum) + 1;

        realEndPage = (int) (Math.ceil(totalCount / (double) perPageNum));

        if (startPage < 1)
            startPage = 1;
        if (endPage > realEndPage) {
            endPage = realEndPage;
        }

        prev = startPage == 1 ? false : true;
        next = endPage < realEndPage ? true : false;

    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;

        calcData();
    }

}
