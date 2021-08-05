package com.flutter.gbsdinspector.service;

import com.flutter.gbsdinspector.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.net.UnknownHostException;

@Slf4j
public class FlinkService {

    private final QueryableStateClient client;

    private static final ReducingStateDescriptor<Customer> selectionViewStateDescriptor =
            new ReducingStateDescriptor<Customer>(
                    "selectionViewState",
                    new CustomerReduce(),
                    Customer.class
            );

    private FlinkService(String host, Integer port) throws UnknownHostException {
        log.info("Initiating connecting with {}:{}", host, port);
        this.client = new QueryableStateClient(host, port);
        client.setExecutionConfig(new ExecutionConfig());
    }

    public Customer querySelectionState(Long key) throws Exception {
        return client.getKvState(JobID.fromHexString("CHANGETHISMOFO"), "QueryableSelectionsState", key, BasicTypeInfo.LONG_TYPE_INFO, selectionViewStateDescriptor).join().get();
    }

    public void close() {
        log.info("Closing connection");
        this.client.shutdownAndWait();
    }


    public static FlinkService init(String host, Integer port) throws UnknownHostException {
        return new FlinkService(host, port);
    }
}
