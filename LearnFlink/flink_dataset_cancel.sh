#!/usr/bin/env bash
basePatn="/Users/apple/Documents/GitHub/flink-1.8/flink-dist/target/flink-1.8-SNAPSHOT-bin/flink-1.8-SNAPSHOT/bin"

keyWord="Streaming"
#taskName=$2
#savepointPath=/root/flink-savepoints/
savepointPath="/Users/apple/Desktop/state/savepointData"
if [ -z "${keyWord}" ]
then
    echo "keyWord 不能为空"
    exit 1
fi

#if [ -z "${taskName}" ]
#then
#    echo "taskName 不能为空"
#    exit 1
#fi
aaa=$(${basePatn}/flink list -r | grep "${keyWord}" | grep '[:0-9]' | awk '{ print $4; }')

bbb=$(${basePatn}/flink cancel -s ${savepointPath} ${aaa} | grep "file:")



echo "dd${bbb}"


#${basePatn}/flink list -r | grep "${keyWord}" | grep '[:0-9]' | awk '{ print $4; }' >> /Users/apple/Documents/GitHub/flink-1.8/LearnFlink/abc.txt
