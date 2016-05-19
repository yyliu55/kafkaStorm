/**
 * Company yimivv.com
 * Copyright (C) 2009-2016 All Rights Reserved.
 */
package com.yimivv.sample.storm;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author liujianlong
 * @version $Id MessageScheme.java, v 0.1 2016-05-16 16:15 liujianlong Exp $$
 */
public class MessageScheme implements Scheme {

    /* (non-Javadoc)
     * @see backtype.storm.spout.Scheme#deserialize(byte[])
     */
    public List<Object> deserialize(byte[] ser) {
        try {
            String msg = new String(ser, "UTF-8");
            return new Values(msg);
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }


    /* (non-Javadoc)
     * @see backtype.storm.spout.Scheme#getOutputFields()
     */
    public Fields getOutputFields() {
        // TODO Auto-generated method stub
        return new Fields("msg");
    }
}
        