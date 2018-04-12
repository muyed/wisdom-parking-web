package com.muye.wp.embed.protocol;

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
}
