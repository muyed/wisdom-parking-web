package com.muye.wp.common.cons;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by muye on 18/2/7.
 */
public enum ProductType {

    CARPORT_DEPOSIT(1, "CD", "车位锁押金"),
    ACCOUNT_DEPOSIT(2, "AD", "账户押金"),
    PARKING_SHARE(3, "PS", "停车共享单"),
    PARKING_TICKET(4, "PT", "停车单"),
    PARKING_TICKET_OVERDUE(5, "PTO", "停车单逾期费用"),
    COUPON_COST(6, "CC", "卡券费用"),

    WITHDRAW_CARPORT_DEPOSIT(10, "WCD", "车位锁押金提取"),
    WITHDRAW_ACCOUNT_DEPOSIT(11, "WAD", "账户押金提取"),
    ;

    private Integer type;
    private String code;
    private String name;

    ProductType(Integer type, String code, String name){
        this.type = type;
        this.code = code;
        this.name = name;
    }

    public static ProductType ofType(Integer type){
        Optional<ProductType> optional = Stream.of(ProductType.values())
                .filter(productType -> productType.type == type)
                .findFirst();
        if (optional.isPresent()) return optional.get();
        return null;
    }

    public static ProductType ofCode(String code){
        Optional<ProductType> optional = Stream.of(ProductType.values())
                .filter(productType -> productType.code == code)
                .findFirst();
        if (optional.isPresent()) return optional.get();
        return null;
    }

    public static ProductType ofName(String name){
        Optional<ProductType> optional = Stream.of(ProductType.values())
                .filter(productType -> productType.name.equals(name))
                .findFirst();
        if (optional.isPresent()) return optional.get();
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
