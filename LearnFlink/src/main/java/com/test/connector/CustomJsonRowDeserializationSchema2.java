package com.test.connector;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.formats.json.JsonRowDeserializationSchema;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 针对错误的数据
 * 1. 不符合传入的数据格式饿数据
 * 2. 为null的数据
 */
public class CustomJsonRowDeserializationSchema2 extends JsonRowDeserializationSchema {
    private final int nameIndex;
    private static Logger LOG = LoggerFactory.getLogger(CustomJsonRowDeserializationSchema.class);
    public CustomJsonRowDeserializationSchema2(TypeInformation<Row> typeInfo, int nameIndex) {
        super(typeInfo);
        this.nameIndex = nameIndex;
    }

    @Override
    public Row deserialize(byte[] message){
        if (message != null){
            try {
                Row row = super.deserialize(message);
                return row;
            }catch (Exception e){
                Row row = new Row(getProducedType().getArity());
                row.setField(nameIndex,"errorData");
                row.setField(nameIndex+1,new String(message));
                LOG.error(e.getMessage(),e);
                return row;
            }
        }else {
            Row row = new Row(getProducedType().getArity());
            row.setField(nameIndex,"errorData");
            row.setField(nameIndex+1,"null data");
            return row;
        }
    }
}
