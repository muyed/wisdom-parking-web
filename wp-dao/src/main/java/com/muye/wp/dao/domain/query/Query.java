package com.muye.wp.dao.domain.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 18/4/3.
 */
public class Query {

    private List<Sort> sorts;
    private List<Range> ranges;

    public <T> Range<T> addRange(){
        if (ranges == null)
            ranges = new ArrayList<>();
        Range<T> range = new Range<>();
        ranges.add(range);
        return range;
    }

    public

}
