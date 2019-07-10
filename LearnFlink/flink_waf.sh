#!/usr/bin/env bash
basePatn="/Users/apple/Documents/AgentJava/flink-master/flink-dist/target/flink-1.7-SNAPSHOT-bin/flink-1.7-SNAPSHOT/bin"
jarPath="/Users/apple/Documents/AgentJava/intellProject/apm_cutter/target/dataset_main.jar"
dataSetId="318"
kafkaserver="192.168.223.100:9092,192.168.223.101:9092,192.168.223.102:9092"
groupId="dataset1"
ips="[\"192.168.5.20\",\"192.168.5.21\",\"192.168.5.148\",\"192.168.5.149\",\"192.168.105.20\",\"192.168.105.21\",\"192.168.105.148\",\"192.168.105.149\"]"
ipsName=""
influxDBUrl="192.168.223.103:8086"
username="admin"
password="123abc"

arg="--bootstrap.servers ${kafkaserver} --group.id ${groupId} --dataSetId ${dataSetId} --ips ${ips} --ipsName ${ipsName} --influxDBUrl ${influxDBUrl} --username ${username} --password
${password}"
echo ${arg}
${basePatn}/flink run  ${jarPath} ${arg}
