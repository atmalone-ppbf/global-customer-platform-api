package com.flutter.gcpapi.service;

import com.flutter.gcpapi.model.Customer;
import com.flutter.gcpapi.model.NicknamedAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class TopsService {

    private final QueryableStateClient client;

    private static final MapStateDescriptor<String, List<Customer>> TOPS_STATE_DESCRIPTOR =
            new MapStateDescriptor<>("topsState", Types.STRING, Types.LIST(TypeInformation.of(Customer.class)));


    private TopsService(Integer port) throws UnknownHostException {
        log.debug("Initiating connecting with localhost:{}", port);
        this.client = new QueryableStateClient("localhost",port);
        client.setExecutionConfig(new ExecutionConfig());
    }

    public Map<String,List<Customer>> queryTopsState(String key) throws Exception {
        MapState<String, List<Customer>> mapState =  client.getKvState(
                JobID.fromHexString("6a0add459864f470954f87f7b0d73a2e"),
                "QueryableNicknamedAccountState",
                key,
                BasicTypeInfo.STRING_TYPE_INFO,
                TOPS_STATE_DESCRIPTOR).join();

        return StreamSupport.stream(mapState.entries().spliterator(), false).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));
    }

    public void close() {
        log.info("Closing connection");
        this.client.shutdownAndWait();
    }


    public static TopsService init(Integer port) throws UnknownHostException {
        return new TopsService(port);
    }
}
