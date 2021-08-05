package com.flutter.gbsdinspector.service;

import com.flutter.gbsdinspector.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.net.UnknownHostException;

@Slf4j
public class CustomerService {

    private final QueryableStateClient client;

    private static final ValueStateDescriptor<Customer> customerStateDescriptor =
            new ValueStateDescriptor<>(
                    "customerState",
                    Customer.class
            );

    private CustomerService(String host, Integer port) throws UnknownHostException {
        log.info("Initiating connecting with {}:{}", host, port);
        this.client = new QueryableStateClient(host, port);
        client.setExecutionConfig(new ExecutionConfig());
    }

    public Customer querySelectionState(String key) throws Exception {
        return client.getKvState(JobID.fromHexString("CHANGETHISMOFO"), "QueryableCustomerState", key, BasicTypeInfo.STRING_TYPE_INFO, customerStateDescriptor).get().value();
    }

    public void close() {
        log.info("Closing connection");
        this.client.shutdownAndWait();
    }


    public static CustomerService init(String host, Integer port) throws UnknownHostException {
        return new CustomerService(host, port);
    }
}
