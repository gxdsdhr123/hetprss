package com.alibaba.dubbo.rpc.filter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

@Activate(group = { "provider", "consumer" })
public class InvokeChainLogFilter implements Filter
{
    private static Logger consoleLog;
    private static Logger fileLog;
    private boolean isRecordToConsole;
    private boolean isRecordToFile;
    private static AtomicInteger autoSeq;
    private static AtomicInteger reqSeq;
    private static int collectRate;
    private static final String format = "%s|%s|%s|%s|%s|%d|%d";
    
    public InvokeChainLogFilter() {
        this.isRecordToConsole = true;
        this.isRecordToFile = true;
    }
    
    @Override
    public Result invoke(final Invoker<?> invoker, final Invocation invocation) throws RpcException {
        final Long begin = System.currentTimeMillis();
        final Result result = invoker.invoke(invocation);
        final Long end = System.currentTimeMillis();
        final int req = InvokeChainLogFilter.reqSeq.incrementAndGet();
        if (req <= InvokeChainLogFilter.collectRate) {
            final URL url = invoker.getUrl();
            final HttpServletRequest request = (HttpServletRequest)RpcContext.getContext().getRequest();
            final String logStr = String.format("%s|%s|%s|%s|%s|%d|%d", begin.toString() + InvokeChainLogFilter.autoSeq.incrementAndGet(), (request != null) ? request.getRemoteAddr() : RpcContext.getContext().getRemoteAddressString(), NetUtils.getLocalAddress().toString() + ":" + url.getPort(), url.getServiceInterface(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(begin)), end - begin, this.mapToStr(url.getParameters()).length());
            if (this.isRecordToConsole && InvokeChainLogFilter.consoleLog != null) {
                InvokeChainLogFilter.consoleLog.info(logStr);
            }
            if (this.isRecordToFile && InvokeChainLogFilter.fileLog != null) {
                InvokeChainLogFilter.fileLog.info(logStr);
            }
        }
        if (req == 100) {
            InvokeChainLogFilter.reqSeq.set(0);
        }
        return result;
    }
    
    private String mapToStr(final Map<String, String> map) {
        String str = "";
        if (map == null) {
            return str;
        }
        for (final String key : map.keySet()) {
            str = str + key + ":" + map.get(key);
        }
        return str;
    }
    
    static {
        InvokeChainLogFilter.consoleLog = LoggerFactory.getLogger("CONSOLE");
        InvokeChainLogFilter.fileLog = LoggerFactory.getLogger("FILE");
        InvokeChainLogFilter.autoSeq = new AtomicInteger(0);
        InvokeChainLogFilter.reqSeq = new AtomicInteger(0);
        InvokeChainLogFilter.collectRate = 1;
    }
}
