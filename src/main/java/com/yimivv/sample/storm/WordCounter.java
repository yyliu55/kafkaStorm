/**
 * Company yimivv.com
 * Copyright (C) 2009-2016 All Rights Reserved.
 */
package com.yimivv.sample.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liujianlong
 * @version $Id WordCounter.java, v 0.1 2016-05-16 10:15 liujianlong Exp $$
 */
public class WordCounter extends BaseRichBolt {

    private static final Log LOG = LogFactory.getLog(WordCounter.class);
    private static final long serialVersionUID = 886149197481637894L;
    private OutputCollector collector;
    private Map<String, AtomicInteger> counterMap;

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
        this.counterMap = new HashMap<String, AtomicInteger>();
    }

    @Override
    public void execute(Tuple input) {
        String message = input.getString(0);
        int count = input.getInteger(1);
        LOG.info("RECV[splitter -> counter] : " + message);
        AtomicInteger ai = this.counterMap.get(message);
        if(ai == null) {
            ai = new AtomicInteger();
            this.counterMap.put(message, ai);
        }
        ai.addAndGet(count);
        collector.ack(input);
    }

    @Override
    public void cleanup() {
        LOG.info("The final result:");
        Iterator<Map.Entry<String, AtomicInteger>> iter = this.counterMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, AtomicInteger> entry = iter.next();
            LOG.info(entry.getKey() + "\t:\t" + entry.getValue().get());
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }
}
    
        