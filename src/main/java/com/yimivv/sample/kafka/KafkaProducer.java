/**
 * Company yimivv.com
 * Copyright (C) 2009-2016 All Rights Reserved.
 */
package com.yimivv.sample.kafka;

import com.yimivv.sample.storm.PropertiesUtil;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;
import java.util.Random;


/**
 * @author liujianlong
 * @version $Id KafkaProducer.java, v 0.1 2016-05-03 17:47 liujianlong Exp $$
 */
public class KafkaProducer {

    private static final Log logger = LogFactory.getLog(KafkaProducer.class);
    private static String topic_request = "topic_yzg_beta_liu";

    public static void main(String[] args) {
        Producer<String, String> producer = null;
        int totalTime = 0;
        try {
            Random rnd = new Random();
            int events = 100;
            // 设置配置属性
            Properties properties = PropertiesUtil.getInstance().getPropertiesByName(PropertiesUtil.KAFKA_PRODUCER);
            ProducerConfig config = new ProducerConfig(properties);
            // 创建producer
            producer = new Producer<String, String>(config);
            // 产生并发送消息
            for (long i = 0; i < events; i++) {
                long start=System.currentTimeMillis();
                String msg = "==================>  " + i;
                KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic_request, msg);
                producer.send(data);
                totalTime += (System.currentTimeMillis() - start);
                logger.info("data:" + data.toString());
            }
            logger.info("totalTime:" + totalTime);
        } catch (Exception e) {
            logger.info("exception:" + e.getMessage());
        } finally {
            if(producer != null) {
                producer.close();
            }
        }
    }
}
    
        