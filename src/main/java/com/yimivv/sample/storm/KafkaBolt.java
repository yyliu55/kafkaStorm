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
import backtype.storm.tuple.Values;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @author liujianlong
 * @version $Id KafkaBolt.java, v 0.1 2016-05-16 9:24 liujianlong Exp $$
 */
public class KafkaBolt extends BaseRichBolt {

    private static final Log LOG = LogFactory.getLog(KafkaBolt.class);
    private static final long serialVersionUID = 886149197481637894L;
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String line = input.getString(0);
        collector.emit(input, new Values(line, 1));
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }

}
        