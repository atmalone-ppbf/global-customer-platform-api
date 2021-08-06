package com.flutter.gcpapi.service;

import com.flutter.gcpapi.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.net.UnknownHostException;

import static com.flutter.gcpapi.GcpApiApplication.JOB_ID;

@Slf4j
public class CustomerService {

    private final QueryableStateClient client;

    private static final ValueStateDescriptor<Customer> CUSTOMER_STATE_DESCRIPTOR =
            new ValueStateDescriptor<>(
                    "customerState",
                    Customer.class);

    private CustomerService(Integer port) throws UnknownHostException {
        log.debug("Initiating connecting with {}", port);
        this.client = new QueryableStateClient("localhost", port);
        client.setExecutionConfig(new ExecutionConfig());
    }

    public Customer queryCustomerState(String key) throws Exception {
        return client.getKvState(
                JobID.fromHexString(JOB_ID),
                "customerState",
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
