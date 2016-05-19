/**
 * Company yimivv.com
 * Copyright (C) 2009-2016 All Rights Reserved.
 */
package com.yimivv.sample.storm;

/**
 * @author liujianlong
 * @version $Id KafkaTopology.java, v 0.1 2016-05-13 14:49 liujianlong Exp $$
 */

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import storm.kafka.*;

import java.util.Properties;

public class KafkaTopology {

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {

        Properties configProperties = PropertiesUtil.getInstance().getPropertiesByName(PropertiesUtil.KAFKA_CONFIG);

        BrokerHosts brokerHosts = new ZkHosts((String)configProperties.get("zookeeper.servers"));
        SpoutConfig spoutConf = new SpoutConfig(brokerHosts,
                (String)configProperties.get("topic"),
                (String)configProperties.get("zookeeper.root"),
                (String)configProperties.get("id"));
        spoutConf.zkRoot  = (String)configProperties.get("zookeeper.root");
        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
//        spoutConf.zkServers = Arrays.asList(new String[] {"192.168.88.134", "192.168.88.133"});
        spoutConf.zkPort = Integer.valueOf((String)configProperties.get("zookeeper.port"));
        spoutConf.forceFromStart = Boolean.valueOf((String)configProperties.get("forceFromStart"));
        spoutConf.startOffsetTime = System.currentTimeMillis();
        spoutConf.useStartOffsetTimeIfOffsetOutOfRange = true;

        TopologyBuilder builder = new TopologyBuilder();
        //接收kafka数据的spout
        builder.setSpout("kafka-reader", new KafkaSpout(spoutConf), 6); // Kafka我们创建了一个6分区的Topic，这里并行度设置为6
        //处理spout数据的bolt，可对数据进行处理过滤等
        builder.setBolt("word-splitter", new KafkaBolt(), 2).shuffleGrouping("kafka-reader");
        //数据处理后的业务bolt
        builder.setBolt("word-counter", new WordCounter()).fieldsGrouping("word-splitter", new Fields("word"));

        Config conf = new Config();
        String name = KafkaTopology.class.getSimpleName();
        //判断是否需要控制台输出
        if (args != null && args.length > 0) {
            // Nimbus host name passed from command line
            conf.put(Config.NIMBUS_HOST, args[0]);
            conf.setNumWorkers(3);
            StormSubmitter.submitTopologyWithProgressBar(name, conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(name, conf, builder.createTopology());
            Thread.sleep(60000);
            cluster.shutdown();
        }
    }
}