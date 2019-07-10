#!/usr/bin/env bash
basePatn="/root/bin"
jarPath="/Users/apple/Documents/AgentJava/intellProject/apm_cutter/target/dataset_main.jar"
dataSetId="318"
kafkaserver="192.168.223.100:9092,192.168.223.101:9092,192.168.223.102:9092"
groupId="dataset2"
ips="[\"192.168.5.16\",\"192.168.5.17\",\"192.168.5.144\",\"192.168.5.145\",\"192.168.105.16\",\"192.168.105.17\",\"192.168.105.144\",\"192.168.105.145\"]"
ipsName=""
influxDBUrl="192.168.223.103:8086"
username="admin"
password="123abc"

arg="--bootstrap.servers ${kafkaserver} --group.id ${groupId} --dataSetId ${dataSetId} --ips ${ips} --ipsName ${ipsName} --influxDBUrl ${influxDBUrl} --username ${username} --password
${password}"
echo ${arg}
${basePatn}/flink run  ${jarPath} ${arg}
