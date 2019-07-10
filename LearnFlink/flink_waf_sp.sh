#!/usr/bin/env bash
basePatn="/root/bin"
jarPath="/root/separator_main.jar"
taskId="285"
kafkaserver="192.168.223.100:9092,192.168.223.101:9092,192.168.223.102:9092"
groupId="separator_waf"
arg="--bootstrap.servers ${kafkaserver} --group.id ${groupId} --dataSetId ${taskId}"
echo ${arg}
${basePatn}/flink run  ${jarPath} ${arg}
