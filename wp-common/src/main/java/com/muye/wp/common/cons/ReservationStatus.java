package com.muye.wp.common.cons;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by muye on 18/2/15.
 */
public enum ReservationStatus {

    UNPAID(0), PAID(1), INVALID(2), PARKING(3), OVERDUE_PARKING(4), FINISH(5), OVERDUE_FINISH(6);

    private int code;

    ReservationStatus(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ReservationStatus getByCode(int code){
        Optional<ReservationStatus> optional = Stream.of(ReservationStatus.values())
                .filter(status -> status.code == code)
                .findFirst();

        if (optional.isPresent()){
            return optional.get();
        }

        return null;
    }
}
