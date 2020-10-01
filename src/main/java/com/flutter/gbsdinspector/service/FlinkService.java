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

    public static final ReducingStateDescriptor<EventView> eventViewStateDescriptor =
            new ReducingStateDescriptor<EventView>(
                    "eventViewState",
                    new EventViewReduce(),
                    EventView.class
            );

    public static final ReducingStateDescriptor<MarketView> marketViewStateDescriptor =
            new ReducingStateDescriptor<MarketView>(
                    "marketViewState",
                    new MarketViewReduce(),
                    MarketView.class
            );

    private static final ReducingStateDescriptor<SelectionView> selectionViewStateDescriptor =
            new ReducingStateDescriptor<SelectionView>(
                    "selectionViewState",
                    new SelectionViewReduce(),
                    SelectionView.class
            );

    private FlinkService(String host, Integer port) throws UnknownHostException {
        log.info("Initiating connecting with {}:{}", host, port);
        this.client = new QueryableStateClient(host, port);
        client.setExecutionConfig(new ExecutionConfig());
    }

    public EventView queryEventState(String jobId, Long key) throws Exception {
        return client.getKvState(JobID.fromHexString(jobId), "QueryableEventsState", key, BasicTypeInfo.LONG_TYPE_INFO, eventViewStateDescriptor).join().get();
    }

    public MarketView queryMarketState(String jobId, Long key) throws Exception {
        return client.getKvState(JobID.fromHexString(jobId), "QueryableMarketsState", key, BasicTypeInfo.LONG_TYPE_INFO, marketViewStateDescriptor).join().get();
    }

    public SelectionView querySelectionState(String jobId, Long key) throws Exception {
        return client.getKvState(JobID.fromHexString(jobId), "QueryableSelectionsState", key, BasicTypeInfo.LONG_TYPE_INFO, selectionViewStateDescriptor).join().get();
    }

    public void close() {
        log.info("Closing connection");
        this.client.shutdownAndWait();
    }


    public static FlinkService init(String host, Integer port) throws UnknownHostException {
        return new FlinkService(host, port);
    }
}
