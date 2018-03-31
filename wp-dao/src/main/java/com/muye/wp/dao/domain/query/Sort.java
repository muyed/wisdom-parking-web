package com.muye.wp.dao.domain.query;

import lombok.Data;

/**
 * Created by muye on 18/3/6.
 */
@Data
public class Sort {

    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private String column;
    private String sort;

    public Sort(String column, String sort){
        this.column = column;
        this.sort = sort;
    }
}
