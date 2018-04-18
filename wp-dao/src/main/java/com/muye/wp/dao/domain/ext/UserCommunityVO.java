package com.muye.wp.dao.domain.ext;

import com.muye.wp.dao.domain.Carport;
import lombok.Data;

import java.util.List;

/**
 * Created by muye on 18/4/18.
 */
@Data
public class UserCommunityVO {

    private Long communityId;
    private Integer type;
    private String reason;
    private String communityName;
    private Integer communityType;
    private String province;
    private String city;
    private String area;
    private String addr;
    private List<Carport> carportList;
}
