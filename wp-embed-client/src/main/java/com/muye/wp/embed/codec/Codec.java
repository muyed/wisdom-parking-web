package com.muye.wp.embed.codec;

import com.muye.wp.embed.protocol.Head;
import com.muye.wp.embed.protocol.Proto;

import java.io.*;
import java.util.List;

/**
 * Created by muye on 18/4/12.
 */
public class Codec {

    public static Proto decode(InputStream in) throws IOException, ClassNotFoundException{

        synchronized (in){
            ObjectInputStream headOis = null;
            ObjectInputStream bodyOis = null;

            try {
                byte[] bytes = new byte[15];
                in.read(bytes);
                headOis = new ObjectInputStream(new ByteArrayInputStream(bytes));
                Head head = (Head) headOis.readObject();

                bytes = new byte[head.getSize()];
                bodyOis = new ObjectInputStream(new ByteArrayInputStream(bytes));
                List<Object> body = (List<Object>) bodyOis.readObject();

                return new Proto(head, body);
            }finally {
                try {
                    if (headOis != null) headOis.close();
                }catch (Exception e){
                }
                try {
                    if (bodyOis != null) bodyOis.close();
                }catch (Exception e){
                }
            }
        }
    }

    public static byte[] encode(Proto proto) throws IOException{

        ObjectOutputStream headOos = null;
        ObjectOutputStream bodyOos = null;

        try {
            ByteArrayOutputStream headBos = new ByteArrayOutputStream();
            ByteArrayOutputStream bodyBos = new ByteArrayOutputStream();

            headOos = new ObjectOutputStream(headBos);
            bodyOos = new ObjectOutputStream(bodyBos);

            headOos.writeObject(proto.getHead());
            bodyOos.writeObject(proto.getBody());

            byte[] headBytes = headBos.toByteArray();
            byte[] bodyBytes = bodyBos.toByteArray();

            byte[] bytes = new byte[headBytes.length + bodyBytes.length];
            System.arraycopy(headBytes, 0, bytes, 0, headBytes.length);
            System.arraycopy(bodyBytes, 0, bytes, headBytes.length, bodyBytes.length);
            return bytes;
        }finally {
            try {
                if (headOos != null) headOos.close();
            }catch (Exception e){
            }
            try {
                if (bodyOos != null) bodyOos.close();
            }catch (Exception e){
            }
        }
    }
}
