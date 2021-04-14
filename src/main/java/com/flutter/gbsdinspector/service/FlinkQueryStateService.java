package com.flutter.gbsdinspector.service;

import com.flutter.gbsd.model.internal.EventView;
import com.flutter.gbsd.model.internal.MarketView;
import com.flutter.gbsd.model.internal.SelectionView;
import com.flutter.gbsdinspector.model.EventViewReduce;
import com.flutter.gbsdinspector.model.MarketViewReduce;
import com.flutter.gbsdinspector.model.SelectionViewReduce;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class FlinkQueryStateService {

    private final QueryableStateClient client;

    private static final ReducingStateDescriptor<EventView> eventViewStateDescriptor =
            new ReducingStateDescriptor<>(
                    "eventViewState",
                    new EventViewReduce(),
                    EventView.class
            );

    private static final ReducingStateDescriptor<MarketView> marketViewStateDescriptor =
            new ReducingStateDescriptor<>(
                    "marketViewState",
                    new MarketViewReduce(),
                    MarketView.class
            );

    private static final ReducingStateDescriptor<SelectionView> selectionViewStateDescriptor =
            new ReducingStateDescriptor<>(
                    "selectionViewState",
                    new SelectionViewReduce(),
                    SelectionView.class
            );

    private static final ValueStateDescriptor<MarketView.EventEvict> eventEvictStateDescriptor =
            new ValueStateDescriptor<>(
                    "eventEvictValueState",
                    MarketView.EventEvict.class
            );

    private static final MapStateDescriptor<Long, Long> selectionsToMarketsEvictMapStateDescriptor =
            new MapStateDescriptor<>(
                    "selectionsToMarketsEvictMapState",
                    Long.class,
                    Long.class);

    private FlinkQueryStateService(String host, Integer port) throws UnknownHostException {
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

    public MarketView.EventEvict queryEventEvictState(String jobId, Long key) throws Exception {
        return client.getKvState(JobID.fromHexString(jobId), "QueryableEventEvictValueState", key, BasicTypeInfo.LONG_TYPE_INFO, eventEvictStateDescriptor).join().value();
    }

    public Map<Long, Long> querySelectionsToMarketsEvictState(String jobId, Long key) throws Exception {
        MapState<Long, Long> mapState = client.getKvState(JobID.fromHexString(jobId), "QueryableSelectionsToMarketsEvictMapState", key, BasicTypeInfo.LONG_TYPE_INFO, selectionsToMarketsEvictMapStateDescriptor).join();
        return StreamSupport.stream(mapState.entries().spliterator(), false).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));
    }

    public void close() {
        log.info("Closing connection");
        this.client.shutdownAndWait();
    }


    public static FlinkQueryStateService init(String host, Integer port) throws UnknownHostException {
        return new FlinkQueryStateService(host, port);
    }
}
