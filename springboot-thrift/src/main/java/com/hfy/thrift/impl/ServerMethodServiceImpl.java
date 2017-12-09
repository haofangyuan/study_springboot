package com.hfy.thrift.impl;

import com.hfy.thrift.ServerMethodService;
import org.apache.thrift.TException;

/**
 * Created by hfy on 2017/12/9.
 */
public class ServerMethodServiceImpl implements ServerMethodService.Iface {
    @Override
    public String serverMethod() throws TException {
        System.out.println("server method execute ... ");
        return "server method execute result";
    }
}
