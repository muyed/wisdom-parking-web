package com.muye.wp.embed.codec;

import com.muye.wp.embed.protocol.Proto;
import com.muye.wp.embed.protocol.ProtoMethod;
import com.muye.wp.embed.protocol.ProtoType;

import java.io.*;
import java.util.List;

/**
 * Created by muye on 18/4/12.
 */
public class Codec {

    public static Proto decode(InputStream in) throws IOException, ClassNotFoundException{

        synchronized (in){
            ObjectInputStream ois = null;

            try {
                Proto proto = new Proto();

                byte[] head = new byte[15];
                in.read(head);

                proto.setVersion(head[0]);
                proto.setAskId(ByteUtil.BytesToLong(head, 1));
                proto.setType(ProtoType.ofType(head[9]));
                proto.setMethod(ProtoMethod.ofMethod(head[10]));
                proto.setSize(ByteUtil.bytesToInt(head, 11));

                byte[] body = new byte[proto.getSize()];
                in.read(body);

                try {
                    ois = new ObjectInputStream(new ByteArrayInputStream(body));
                    proto.setBody((List<Object>) ois.readObject());
                }catch (EOFException e){
                }

                return proto;
            }finally {
                try {
                    if (ois != null) ois.close();
                }catch (Exception e){
                }
            }
        }
    }

    public static byte[] encode(Proto proto) throws IOException{

        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;

        try {
            //head
            byte[] head = new byte[15];
            head[0] = proto.getVersion();
            System.arraycopy(ByteUtil.LongToBytes(proto.getAskId()), 0, head, 1, 8);
            head[9] = proto.getType().getType();
            head[10] = proto.getMethod().getMethod();

            //body
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(proto.getBody());
            byte[] body = bos.toByteArray();
            proto.setSize(body.length);

            System.arraycopy(ByteUtil.intToBytes(proto.getSize()), 0, head, 11, 4);

            byte[] bytes = new byte[15 + body.length];
            System.arraycopy(head, 0, bytes, 0, 15);
            System.arraycopy(body, 0, bytes, 15, body.length);

            return bytes;
        }finally {
            try {
                if (oos != null) oos.close();
            }catch (Exception e){
            }
            try {
                if (bos != null) bos.close();
            }catch (Exception e){
            }
        }
    }
}
