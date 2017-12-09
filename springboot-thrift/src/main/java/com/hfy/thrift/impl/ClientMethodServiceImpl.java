package com.hfy.thrift.impl;

import com.hfy.thrift.ClientMethodService;
import org.apache.thrift.TException;

/**
 * Created by hfy on 2017/12/9.
 */
public class ClientMethodServiceImpl implements ClientMethodService.Iface {
    @Override
    public String clientMethod() throws TException {
        System.out.println("client method execute ... ");
        return "client method execute result";
    }
}
