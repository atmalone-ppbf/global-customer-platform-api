package com.flutter.gbsi.operators;

import com.flutter.gbsd.model.internal.SelectionView;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.state.api.functions.KeyedStateReaderFunction;
import org.apache.flink.util.Collector;

public class NicknamingDecoratorReader extends KeyedStateReaderFunction<String, String> {

    public static final ValueStateDescriptor<String> NICKNAME_STATE_DESCRIPTOR =
            new ValueStateDescriptor<String>("nicknameState", Types.STRING);

    protected transient ValueState<String> nicknameState;

    @Override
    public void open(Configuration configuration) throws Exception {
        nicknameState = getRuntimeContext().getState(NICKNAME_STATE_DESCRIPTOR);
    }

    @Override
    public void readKey(String key, KeyedStateReaderFunction.Context context, Collector<String> collector) throws Exception {

    }
}
