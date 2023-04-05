package com.uet.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryModel<T> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageModel {
        private Integer currentPage;
        private Integer pageSize;
        private Integer totalPage;
        private Long totalElement = 0L;

        public PageModel(Integer currentPage, Integer pageSize, Integer totalPage) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalPage = totalPage;
        }

        public PageModel(Page page) {
            this.currentPage = page.getPageable().getPageNumber();
            this.pageSize = page.getPageable().getPageSize();
            this.totalPage = page.getTotalPages();
            this.totalElement = page.getTotalElements();
        }
    }

    private PageModel page;
    private List<T> elements;


}
