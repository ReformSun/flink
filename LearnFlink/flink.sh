#!/usr/bin/env bash


basePatn="/Users/apple/Documents/GitHub/flink-1.8/flink-dist/target/flink-1.8-SNAPSHOT-bin/flink-1.8-SNAPSHOT/bin"
jarPath="/Users/apple/Documents/GitHub/flink-1.8/LearnFlink/target"
#savepointPath="/Users/apple/Desktop/state/savepointData/"
#savepointPath="hdfs://localhost:9000/flink-checkpoints"
#target="$0"
#iteration=0
#while [ -L "$target" ]; do
#    if [ "$iteration" -gt 100 ]; then
#        echo "Cannot resolve path: You have a cyclic symlink in $target."
#        break
#    fi
#    ls=`ls -ld -- "$target"`
#    target=`expr "$ls" : '.* -> \(.*\)$'`
#    echo "$target"
#    iteration=$((iteration + 1))
#done
# 具体命令行查看 （https://ci.apache.org/projects/flink/flink-docs-release-1.7/ops/cli.html）

#${basePatn}/flink -h

# 运行flink job
${basePatn}/flink run ${jarPath}/${1}
# 运行flink job 指定主类
#${basePatn}/flink run -c org.apache.flink.examples.java.wordcount.WordCount ${jarPath}/${1}
# 运行关闭的任务 从指定的savepointPath
#${basePatn}/flink run -s /root/flink-savepoints/savepoint-123777-91f836c80b81 ${jarPath}/${1}


# 指定jobmanager
#${basePatn}/flink run -m 10.4.247.17:8081 ${jarPath}/${1}


#${basePatn}/flink list -a
# 查看正在运行的job
#${basePatn}/flink list -r #查看全部正在运行的job任务

# 关闭正在运行的job
#${basePatn}/flink cancel 10356e75cd768aa31e138d2d95303cf5 #关闭指定job 怎么查看jobid #${basePatn}/flink list -r
#${basePatn}/flink list -r | grep '[:0-9]' | awk '{ print $4; }' | xargs -I ar ${basePatn}/flink cancel  ar

# 触发检查点 指定jobid
#${basePatn}/flink savepoint ac30e1322dc9f39e49c88d4eba5254e8 ${savepointPath}
#${basePatn}/flink list -r | grep '[:0-9]' | awk '{ print $4; }' | xargs -I ar ${basePatn}/flink savepoint ar ${savepointPath}
#${basePatn}/flink list -r | grep '[:0-9]' | awk '{ print $4; }' | xargs -I ar ${basePatn}/flink savepoint ar
# 关闭并触发检查点 可指定检查点位置信息 指定jobid
#${basePatn}/flink cancel -s 10356e75cd768aa31e138d2d95303cf5
# 关闭但不触发检查点
#${basePatn}/flink list -r | grep '[:0-9]' | awk '{ print $4; }' | xargs -I ar ${basePatn}/flink cancel  ar


# 触发savepoint
#${basePatn}/flink list -r | grep '[:0-9]' | awk '{ print $4; }' | xargs -I ar ${basePatn}/flink savepoint ar ${savepointPath}
# 触发savepoint 并关闭任务
#${basePatn}/flink list -r | grep '[:0-9]' | awk '{ print $4; }' | xargs -I ar ${basePatn}/flink cancel -s ${savepointPath} ar
