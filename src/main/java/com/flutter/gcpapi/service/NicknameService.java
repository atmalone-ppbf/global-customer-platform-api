package com.flutter.gcpapi.service;

import com.flutter.gcpapi.model.NicknamedAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class NicknameService {

    private final QueryableStateClient client;

    private static final MapStateDescriptor<String, NicknamedAccount> nicknamedAccountStateDescriptor =
            new MapStateDescriptor<>(
                    "nicknamedAccountState",
                    Types.STRING,
                    Types.POJO(NicknamedAccount.class)
            );

    private NicknameService(String host, Integer port) throws UnknownHostException {
        log.info("Initiating connecting with {}:{}", host, port);
        this.client = new QueryableStateClient(host, port);
        client.setExecutionConfig(new ExecutionConfig());
    }

    public Map<String,NicknamedAccount> queryNicknamedAccountState(String key) throws Exception {
        MapState<String, NicknamedAccount> mapState =  client.getKvState(
                JobID.fromHexString("CHANGETHISMOFO"),
                "QueryableNicknamedAccountState",
                key,
                BasicTypeInfo.STRING_TYPE_INFO,
                nicknamedAccountStateDescriptor).join();

        return StreamSupport.stream(mapState.entries().spliterator(), false).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));
    }

    public void close() {
        log.info("Closing connection");
        this.client.shutdownAndWait();
    }


    public static NicknameService init(String host, Integer port) throws UnknownHostException {
        return new NicknameService(host, port);
    }
}
