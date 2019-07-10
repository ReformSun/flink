package com.test.connector;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.kafka.config.StartupMode;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;
import org.apache.flink.util.Preconditions;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class JsonSourceBuilder {
    private String topic;
    private Properties kafkaProps;
    private TableSchema schema;
    private StartupMode startupMode = StartupMode.GROUP_OFFSETS;
    private Map<KafkaTopicPartition, Long> specificStartupOffsets = null;
    private long startTime;
    private boolean enableCommitOnCheckpoints = true;
    private DeserializationSchema deserializationSchema;
    public static JsonSourceBuilder builder(){
        return new JsonSourceBuilder();
    }

    public JsonSourceBuilder forTopic(String topic) {
        Preconditions.checkNotNull(topic, "Topic must not be null.");
        Preconditions.checkArgument(this.topic == null, "Topic has already been set.");
        this.topic = topic;
        return this;
    }

    public JsonSourceBuilder withKafkaProperties(Properties props) {
        Preconditions.checkNotNull(props, "kafkaProps must not be null.");
        Preconditions.checkArgument(this.kafkaProps == null, "kafkaProps have already been set.");
        this.kafkaProps = props;
        return this;
    }

    public JsonSourceBuilder withSchema(TableSchema schema) {
        Preconditions.checkNotNull(schema, "Schema must not be null.");
        Preconditions.checkArgument(this.schema == null, "Schema has already been set.");
        this.schema = schema;
        return this;
    }

    /**
     * 是否启动当检查点结束后进行偏移量提交
     * 在这里我默认启动
     * flink自身默认为周期性提交
     * @param enableCommitOnCheckpoints
     * @return
     */
    public JsonSourceBuilder withEnableCommitOnCheckpoints(boolean enableCommitOnCheckpoints) {
        this.enableCommitOnCheckpoints = enableCommitOnCheckpoints;
        return this;
    }
    public JsonSourceBuilder withDeserializationSchema(DeserializationSchema deserializationSchema) {
        Preconditions.checkNotNull(deserializationSchema, "deserializationSchema must not be null.");
        Preconditions.checkArgument(this.deserializationSchema == null, "deserializationSchema has already been set.");
        this.deserializationSchema = deserializationSchema;
        return this;
    }

    /**
     * 设置kafka开始模式
     * 默认设置为GROUP_OFFSET
     * @param startupMode
     * @return
     */
    public JsonSourceBuilder withStartupMode(StartupMode startupMode) {
        Preconditions.checkNotNull(startupMode, "startupMode must not be null.");
        this.startupMode = startupMode;
        return this;
    }

    /**
     * 设置kafka开始模式
     * @param startupMode
     * @param specificStartupOffsets 指定开始偏移量
     * @return
     */
    public JsonSourceBuilder withSpecificStartupOffsets(StartupMode startupMode, Map<KafkaTopicPartition, Long> specificStartupOffsets) {
        Preconditions.checkNotNull(startupMode, "startupMode must not be null.");
        Preconditions.checkArgument(startupMode != StartupMode.SPECIFIC_OFFSETS,"StartupMode must not be SPECIFIC_OFFSETS");
        Preconditions.checkNotNull(specificStartupOffsets,"specificStartupOffsets must not be isEmpty.");
        Preconditions.checkArgument(specificStartupOffsets.isEmpty(),"specificStartupOffsets must not be null.");

        this.specificStartupOffsets = specificStartupOffsets;
        this.startupMode = startupMode;
        return this;
    }
    /**
     * 设置kafka开始模式
     * @param startupMode
     * @param startTime 指定开始时间
     * @return
     */
    public JsonSourceBuilder withStartTime(StartupMode startupMode, Long startTime) {
        Preconditions.checkNotNull(startupMode, "startupMode must not be null.");
        Preconditions.checkArgument(startupMode != StartupMode.TIMESTAMP,"StartupMode must not be TIMESTAMP");
        Preconditions.checkNotNull(startTime,"specificStartupOffsets must not be isEmpty.");
        Preconditions.checkArgument(startTime == 0,"specificStartupOffsets must not be null.");
        this.startTime = startTime;
        this.startupMode = startupMode;
        return this;
    }

    public SourceFunction<Row> build(){
        return createKafka011Source();
    }

    private FlinkKafkaConsumerBase<Row> createKafka011Source(){
        if (specificStartupOffsets == null){
            specificStartupOffsets = Collections.emptyMap();
        }
        FlinkKafkaConsumer011<Row> flinkKafkaConsumer011 = new FlinkKafkaConsumer011(topic,deserializationSchema,kafkaProps);
        switch (startupMode){
            case LATEST:flinkKafkaConsumer011.setStartFromLatest();break;
            case EARLIEST:flinkKafkaConsumer011.setStartFromEarliest();break;
            case TIMESTAMP:flinkKafkaConsumer011.setStartFromTimestamp(startTime);break;
            case SPECIFIC_OFFSETS:flinkKafkaConsumer011.setStartFromSpecificOffsets(specificStartupOffsets);break;
            case GROUP_OFFSETS:flinkKafkaConsumer011.setStartFromGroupOffsets();break;
            default:flinkKafkaConsumer011.setStartFromGroupOffsets();break;
        }

        if (enableCommitOnCheckpoints){
            flinkKafkaConsumer011.setCommitOffsetsOnCheckpoints(true);
        }else {
            flinkKafkaConsumer011.setCommitOffsetsOnCheckpoints(false);
        }


        return flinkKafkaConsumer011;
    }

}
