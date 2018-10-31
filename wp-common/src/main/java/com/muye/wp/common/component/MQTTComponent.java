package com.muye.wp.common.component;

import com.aliyun.openservices.ons.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * created by dahan at 2018/10/26
 */
@Component
public class MQTTComponent implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTComponent.class);

    private static final String ACCESS_KEY = "LTAIqGaobbCeE1Fk";

    private static final String SECRET_KET = "rh4BtPp0lvBV2rECUlZEbELV912eiT";

    private static final String TOPIC = "PARKING_CARPORT";

    private static final String GROUP_ID = "GID_PARKING_CARPORT";

    private static final String PRODUCER_ID = "PID_PC_PRO";

    private static final String CONSUMER_ID = "CID_PC_CON";

    private Producer producer;

    private Consumer consumer;

    @PostConstruct
    public void init(){
        final Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, PRODUCER_ID);
        properties.put(PropertyKeyConst.AccessKey, ACCESS_KEY);
        properties.put(PropertyKeyConst.SecretKey, SECRET_KET);

        producer = ONSFactory.createProducer(properties);
        producer.start();


    }

    public void send(String deviceId, String tag, String body) throws Exception{
        Message message = new Message(TOPIC, tag, body.getBytes("UTF-8"));
        message.putUserProperties("mqttSecondTopic", "/p2p/" + GROUP_ID + "@@@" + deviceId);
        producer.send(message);
    }


    public void listener(){
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, CONSUMER_ID);
        properties.put(PropertyKeyConst.AccessKey, ACCESS_KEY);
        properties.put(PropertyKeyConst.SecretKey, SECRET_KET);
        consumer =ONSFactory.createConsumer(properties);

        consumer.subscribe(TOPIC, "*", ((message, consumeContext) -> {
            try {
                LOGGER.info("mqttmq body:" + new String(message.getBody(), "utf-8"));
                return Action.CommitMessage;
            } catch (Exception e) {
                return Action.ReconsumeLater;
            }
        }));
    }

    @Override
    public void afterPropertiesSet() {
        new Thread(() -> {
            for (;;){

                LOGGER.info("send test...");

                try {
                    send("MQTTTEST", "testTag", "test body");
                    Thread.sleep(1000 * 30);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


        listener();
    }
}
