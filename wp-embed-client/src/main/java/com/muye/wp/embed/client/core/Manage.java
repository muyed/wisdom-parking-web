//package com.muye.wp.embed.client.core;
//
//import com.muye.wp.embed.codec.Codec;
//import com.muye.wp.embed.mode.TempCarLicense;
//import com.muye.wp.embed.protocol.Head;
//import com.muye.wp.embed.protocol.Proto;
//import com.muye.wp.embed.protocol.ProtoMethod;
//import com.muye.wp.embed.protocol.ProtoType;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
///**
// * Created by muye on 18/4/12.
// */
//public class Manage {
//
//    HashSet<String> carLicenseList = new HashSet<>();   //业主车牌
//    HashSet<TempCarLicense> tempList = new HashSet<>(); //临时车牌
//
//
//    private Client client;
//
//    public void start() throws Exception{
//        client = Client.instance();
//        client.start();
//
//
//
//        while(true) {
//            Proto proto = Codec.decode(client.in);
//
//        }
//    }
//
//    public void reg(){
//        Head head = new Head();
//        head.setMethod(ProtoMethod.INIT.getMethod());
//        head.setType(ProtoType.ASK.getType());
//
//        List<>
//    }
//}
