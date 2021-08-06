package com.flutter.gcpapi.service;

import com.flutter.gcpapi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class CustomerService {

    private final QueryableStateClient client;

    private static final ValueStateDescriptor<Customer> CUSTOMER_STATE_DESCRIPTOR =
            new ValueStateDescriptor<>("customerState", Customer.class);

    private CustomerService(Integer port) throws UnknownHostException {
        log.info("Initiating connecting with {}", port);
        this.client = new QueryableStateClient("localhost", port);
        client.setExecutionConfig(new ExecutionConfig());
    }

    public Customer queryCustomerState(String key) throws Exception {
        return client.getKvState(
                JobID.fromHexString("6a0add459864f470954f87f7b0d73a2e"),
                "QueryableCustomerState",
                key,
                BasicTypeInfo.STRING_TYPE_INFO,
                CUSTOMER_STATE_DESCRIPTOR).get().value();
    }

    public void close() {
        log.info("Closing connection");
        this.client.shutdownAndWait();
    }


    public static CustomerService init(Integer port) throws UnknownHostException {
        return new CustomerService(port);
    }
}
