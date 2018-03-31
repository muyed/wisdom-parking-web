package com.muye.wp.dao.domain.query;

import com.muye.wp.dao.domain.Carport;
import lombok.Data;

import java.util.List;

/**
 * Created by muye on 18/3/6.
 */
@Data
public class CarportQuery extends Carport {

    private List<Sort> sortList;
}
