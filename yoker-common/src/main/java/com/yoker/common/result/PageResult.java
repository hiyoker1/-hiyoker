package com.yoker.common.result;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {

    private Long total;
    private List<T> list;

    public static <T> PageResult<T> of(Long total, List<T> list) {
        PageResult<T> page = new PageResult<>();
        page.setTotal(total);
        page.setList(list);
        return page;
    }
}