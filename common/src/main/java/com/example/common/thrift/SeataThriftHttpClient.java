package com.example.common.thrift;

import io.seata.core.context.RootContext;
import org.springframework.util.StringUtils;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeataThriftHttpClient extends THttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeataThriftHttpClient.class);

    public SeataThriftHttpClient(String url) throws TTransportException {
        super(url);
    }

    @Override
    public void flush() throws TTransportException {
        String xid = RootContext.getXID();
        if (StringUtils.hasText(xid)) {
            setCustomHeader(RootContext.KEY_XID, xid);
            LOGGER.info("Propagating global transaction XID via Thrift: xid={}", xid);
        }
        super.flush();
    }
}
