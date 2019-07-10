package com.test.connector;

import org.apache.flink.streaming.connectors.kafka.Kafka011TableSource;
import org.apache.flink.streaming.connectors.kafka.KafkaTableSourceBase;
import org.apache.flink.streaming.connectors.kafka.config.StartupMode;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.sources.RowtimeAttributeDescriptor;
import org.apache.flink.table.sources.tsextractors.StreamRecordTimestamp;
import org.apache.flink.table.sources.tsextractors.TimestampExtractor;
import org.apache.flink.table.sources.wmstrategies.WatermarkStrategy;
import org.apache.flink.util.Preconditions;

import java.util.*;

public class JsonTableSourceBuilder {
    private TableSchema jsonSchema;
    private Map<String, String> fieldMapping;
    private boolean failOnMissingField = false;
    private String topic;
    private Properties kafkaProps;
    private TableSchema schema;
    private String proctimeAttribute;
    private RowtimeAttributeDescriptor rowtimeAttributeDescriptor;
    private StartupMode startupMode = StartupMode.GROUP_OFFSETS;
    private Map<KafkaTopicPartition, Long> specificStartupOffsets = null;
    public static JsonTableSourceBuilder builder(){
        return new JsonTableSourceBuilder();
    }

    public JsonTableSourceBuilder forTopic(String topic) {
        Preconditions.checkNotNull(topic, "Topic must not be null.");
        Preconditions.checkArgument(this.topic == null, "Topic has already been set.");
        this.topic = topic;
        return this;
    }

    public JsonTableSourceBuilder withKafkaProperties(Properties props) {
        Preconditions.checkNotNull(props, "Properties must not be null.");
        Preconditions.checkArgument(this.kafkaProps == null, "Properties have already been set.");
        this.kafkaProps = props;
        return this;
    }

    public JsonTableSourceBuilder withSchema(TableSchema schema) {
        Preconditions.checkNotNull(schema, "Schema must not be null.");
        Preconditions.checkArgument(this.schema == null, "Schema has already been set.");
        this.schema = schema;
        return this;
    }

    public JsonTableSourceBuilder withProctimeAttribute(String proctimeAttribute) {
        Preconditions.checkNotNull(proctimeAttribute, "Proctime attribute must not be null.");
        Preconditions.checkArgument(!proctimeAttribute.isEmpty(), "Proctime attribute must not be empty.");
        Preconditions.checkArgument(this.proctimeAttribute == null, "Proctime attribute has already been set.");
        this.proctimeAttribute = proctimeAttribute;
        return this;
    }
    public JsonTableSourceBuilder withRowtimeAttribute(
            String rowtimeAttribute,
            TimestampExtractor timestampExtractor,
            WatermarkStrategy watermarkStrategy) {
        Preconditions.checkNotNull(rowtimeAttribute, "Rowtime attribute must not be null.");
        Preconditions.checkArgument(!rowtimeAttribute.isEmpty(), "Rowtime attribute must not be empty.");
        Preconditions.checkNotNull(timestampExtractor, "Timestamp extractor must not be null.");
        Preconditions.checkNotNull(watermarkStrategy, "Watermark assigner must not be null.");
        Preconditions.checkArgument(this.rowtimeAttributeDescriptor == null,
                "Currently, only one rowtime attribute is supported.");

        this.rowtimeAttributeDescriptor = new RowtimeAttributeDescriptor(
                rowtimeAttribute,
                timestampExtractor,
                watermarkStrategy);
        return this;
    }

    public JsonTableSourceBuilder withKafkaTimestampAsRowtimeAttribute(
            String rowtimeAttribute,
            WatermarkStrategy watermarkStrategy) {

        Preconditions.checkNotNull(rowtimeAttribute, "Rowtime attribute must not be null.");
        Preconditions.checkArgument(!rowtimeAttribute.isEmpty(), "Rowtime attribute must not be empty.");
        Preconditions.checkNotNull(watermarkStrategy, "Watermark assigner must not be null.");
        Preconditions.checkArgument(this.rowtimeAttributeDescriptor == null,
                "Currently, only one rowtime attribute is supported.");
//        Preconditions.checkArgument(supportsKafkaTimestamps(), "Kafka timestamps are only supported since Kafka 0.10.");

        this.rowtimeAttributeDescriptor = new RowtimeAttributeDescriptor(
                rowtimeAttribute,
                new StreamRecordTimestamp(),
                watermarkStrategy);
        return this;
    }

    public KafkaTableSourceBase build(){
        return createKafka011TableSource();
    }

    private KafkaTableSourceBase createKafka011TableSource(){
        Optional<String> optional =  null;
        if (proctimeAttribute != null){
            optional = Optional.of(proctimeAttribute);
        }else {
            optional = Optional.empty();
        }

        List<RowtimeAttributeDescriptor> rowtimeAttributeDescriptors = null;
        if (rowtimeAttributeDescriptor != null){
            rowtimeAttributeDescriptors = new ArrayList<>();
            rowtimeAttributeDescriptors.add(rowtimeAttributeDescriptor);
        }

        Optional<Map<String,String>> optional1 = null;
        if (fieldMapping != null){
            optional1 = Optional.of(fieldMapping);
        }else {
            optional1 = Optional.empty();
        }

        if (specificStartupOffsets == null){
            specificStartupOffsets = Collections.emptyMap();
        }

        CustomJsonRowDeserializationSchema customJsonRowDeserializationSchema = new CustomJsonRowDeserializationSchema(schema.toRowType());

        return new Kafka011TableSource(schema,optional,rowtimeAttributeDescriptors,optional1,topic,kafkaProps,customJsonRowDeserializationSchema,startupMode,specificStartupOffsets);
    }




}
