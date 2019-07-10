package com.test.connector;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.formats.json.JsonRowDeserializationSchema;
import org.apache.flink.types.Row;

public class CustomJsonRowDeserializationSchema extends JsonRowDeserializationSchema {
    public CustomJsonRowDeserializationSchema(TypeInformation<Row> typeInfo) {
        super(typeInfo);
    }

    @Override
    public Row deserialize(byte[] message){
        try {
            Row row = super.deserialize(message);
            return row;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
