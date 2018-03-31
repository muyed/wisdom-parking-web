package com.muye.wp.common.cons;

import java.util.stream.Stream;

/**
 * Created by muye on 18/1/25.
 */
public enum UserType {

    GENERAL(1, "普通用户"), PROPERTY(2, "物业"), OPERATOR(3, "运营人员");

    private int type;
    private String typeName;

    UserType(int type, String typeName){
        this.type = type;
        this.typeName = typeName;
    }

    public static UserType of(int type){
        return Stream.of(UserType.values())
                .filter(userType -> userType.type == type)
                .findFirst()
                .get();
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
