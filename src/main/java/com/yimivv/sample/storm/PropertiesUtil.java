/**
 * Company yimivv.com
 * Copyright (C) 2009-2016 All Rights Reserved.
 */
package com.yimivv.sample.storm;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author liujianlong
 * @version $Id PropertiesUtil.java, v 0.1 2016-05-05 12:32 liujianlong Exp $$
 */
public class PropertiesUtil {

    private static Logger logger = Logger.getLogger("KAFKASAMPLE");
    private static PropertiesUtil propertiesUtil;
    public static final String KAFKA_CONFIG = "kafka-config.properties";
    public static final String KAFKA_PRODUCER = "kafka-producer.properties";
    private Properties kafkaConfigProperties;
    private Properties producerProperties;

    public static PropertiesUtil getInstance() {
        if(propertiesUtil == null) {
            propertiesUtil = new PropertiesUtil();
            propertiesUtil.init();
        }
        return propertiesUtil;
    }

    public void init() {
        kafkaConfigProperties = new Properties();
        producerProperties = new Properties();
        getPropertiesByFileName(kafkaConfigProperties, KAFKA_CONFIG);
        getPropertiesByFileName(producerProperties, KAFKA_PRODUCER);
    }

    private void getPropertiesByFileName(Properties properties, String fileName){

        InputStream inStream = null;
        try {
            inStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
            properties.load(inStream);
        } catch (IOException e) {
            logger.info("配置文件加载失败!");
        } finally {
            if(inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    logger.info("关闭数据流异常!");
                }
            }
        }
    }

    public Properties getPropertiesByName(String tags) {
        if(tags.equals(KAFKA_CONFIG)) {
            return kafkaConfigProperties;
        } else if(tags.equals(KAFKA_PRODUCER)){
            return producerProperties;
        } else {
            return null;
        }
    }

}
    
        