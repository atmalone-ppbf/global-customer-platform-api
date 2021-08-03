package com.flutter.gbsi.service;

import com.flutter.gbsd.model.internal.EventView;
import com.flutter.gbsd.model.internal.MarketView;
import com.flutter.gbsd.model.internal.SelectionView;
import com.flutter.gbsi.model.SavepointAnalysis;
import com.flutter.gbsi.operators.EventDecoratorReader;
import com.flutter.gbsi.operators.MarketDecoratorReader;
import com.flutter.gbsi.operators.NicknamingDecoratorReader;
import com.flutter.gbsi.operators.SelectionDecoratorReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.state.api.ExistingSavepoint;
import org.apache.flink.state.api.Savepoint;

import java.util.List;

@Slf4j
public class FlinkSavepointAnalysisService {

    public static SavepointAnalysis analyseSavepoint(String path) throws Exception {
        ExecutionEnvironment env   = ExecutionEnvironment.getExecutionEnvironment();

        ExistingSavepoint savepoint = Savepoint.load(env, path, new RocksDBStateBackend(path));

        DataSet<EventView> eventViewState = savepoint.readKeyedState("eventDecoration", new EventDecoratorReader(), Types.LONG, Types.GENERIC(EventView.class));
        DataSet<MarketView> marketViewState = savepoint.readKeyedState("marketDecoration", new MarketDecoratorReader());
        DataSet<SelectionView> selectionViewState = savepoint.readKeyedState("selectionDecoration", new SelectionDecoratorReader());
        DataSet<String> nicknameState = savepoint.readKeyedState("nicknameDecoration", new NicknamingDecoratorReader());

         List<EventView> eventsWithoutStartTime = eventViewState
                .filter(ew -> ew.getEventScheduledStartTime() == null)
                .collect();

        List<EventView> oldestEventViews = eventViewState
                .filter(ew -> ew.getEventScheduledStartTime() != null)
                .sortPartition(EventView::getEventScheduledStartTime, Order.ASCENDING)
                .first(10)
                .collect();

        List<Tuple2<Long, Long>> eventsWithMostMarkets = marketViewState
                .map(m -> Tuple2.of(m.getEventId(), 1L))
                .returns(Types.TUPLE(Types.LONG, Types.LONG))
                .groupBy(0)
                .reduce((a,b) -> Tuple2.of(a.f0, a.f1 + b.f1))
                .sortPartition(1, Order.DESCENDING)
                .first(10)
                .collect();

        List<Tuple2<Long, Long>> marketsWithMostSelections = selectionViewState
                .map(s -> Tuple2.of(s.getMarketId(), 1L))
                .returns(Types.TUPLE(Types.LONG, Types.LONG))
                .groupBy(0)
                .reduce((a,b) -> Tuple2.of(a.f0, a.f1 + b.f1))
                .sortPartition(1, Order.DESCENDING)
                .first(10)
                .collect();

        List<String> allNickNames = nicknameState.sortPartition(0, Order.ASCENDING).collect();

        return SavepointAnalysis.builder()
                .totalEvents(eventViewState.count())
                .totalMarkets(marketViewState.count())
                .totalSelections(selectionViewState.count())
                .oldestEventViews(oldestEventViews)
                .eventsWithoutStartTime(eventsWithoutStartTime)
                .eventsWithMostMarkets(eventsWithMostMarkets)
                .marketsWithMostSelections(marketsWithMostSelections)
                .allNicknames(allNickNames)
                .build();
    }

}
