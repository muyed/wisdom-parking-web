package com.muye.wp.common.cons;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by muye on 18/4/20.
 */
public enum  Bank {

    ICBC(1002, "工商银行"),
    ABC(1005, "农业银行"),
    BOC(1026, "中国银行"),
    CCB(1003, "建设银行"),
    CMB(1001, "招商银行"),
    PSBC(1066, "邮储银行"),
    BCM(1020, "交通银行"),
    SPDB(1004, "浦发银行"),
    CMBC(1006, "民生银行"),
    CIB(1009, "兴业银行"),
    PAB(1010, "平安银行"),
    CNCB(1021, "中信银行"),
    HXB(1025, "华夏银行"),
    CGB(1027, "广发银行"),
    CEB(1022, "光大银行"),
    BOB(1032, "北京银行"),
    NBCB(1056, "宁波银行"),
    ;

    private int bankCode;
    private String bankName;

    Bank(int bankCode, String bankName){
        this.bankCode = bankCode;
        this.bankName = bankName;
    }

    public int getBankCode() {
        return bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public static Bank ofBankCode(int bankCode){
        Optional<Bank> optional = Stream.of(Bank.values())
                .filter(bank -> bank.bankCode == bankCode)
                .findFirst();

        if (optional.isPresent()) return optional.get();
        return null;
    }
}