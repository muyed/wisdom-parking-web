package com.muye.wp.dao.domain.query;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 18/4/3.
 */
@Data
public class Query {

    private List<Sort> sorts;
    private List<Range> ranges;

    public <T> Range<T> addRange(){
        if (ranges == null)
            ranges = new ArrayList<>();
        Range<T> range = new Range<>();
        range.setQuery(this);
        ranges.add(range);
        return range;
    }

    public Query addSort(String column, String sort){
        if (sorts == null)
            sorts = new ArrayList<>();
        sorts.add(new Sort(column, sort));
        return this;
    }

    public void clean(){
        sorts = null;
        ranges = null;
    }
}
