package com.muye.wp.embed.protocol;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by muye on 18/4/12.
 */
public enum ProtoType {

    ASK((byte)0), ANSWER((byte)1);

    private byte type;

    ProtoType(byte type){
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public static ProtoType ofType(byte type){
        Optional<ProtoType> optional = Stream.of(ProtoType.values())
                .filter(protoType -> protoType.type == type)
                .findFirst();
        if (optional.isPresent()) return optional.get();
        return null;
    }
}
