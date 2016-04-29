# Gobblin fork to replace [Liquidm Camus](http://github.com/liquidm/camus)

Looks like Camus is not atomic in all cases and Camus is superseeded by Gobblin anyway.

## Changes

This fork is optimized to read JSON from Kafka and write gzip'ed time-partitioned JSON into HDFS

## Caveats

Currently extracting the timestamp is hardcoded, you will need to adjust

   - [The name of the JSON key containing the timestamp](https://github.com/liquidm/gobblin/blob/master/gobblin-core/src/main/java/gobblin/writer/partitioner/TimestampedObject.java#L4)
   - [The timestamp conversion to epoc](https://github.com/liquidm/gobblin/blob/master/gobblin-core/src/main/java/gobblin/writer/partitioner/TimeBasedJsonWriterPartitioner.java#L37)

## Example config
```
mapreduce.job.queuename=your_queue # optional, ideal for hadoop node labels
job.name=must_be_unique # we use kafkacluster_topic
job.group=GobblinKafka
job.description=Gobblin for kafkacluster_topic
job.lock.enabled=true # avoid races on concurrent starts
job.commit.policy=full # only commit data on total completion

kafka.brokers=kafka1.fqdn:port,kafka2.fqdn:port
topic.whitelist=topic # we use one config per topic

bootstrap.with.offset=latest # or earliest
reset.on.offset.out.of.range=nearest # or earliest / latest

source.class=gobblin.source.extractor.extract.kafka.KafkaSimpleSource
# if you want to limit the maximum time per run
extract.limit.enabled=true
extract.limit.time.limit.timeunit=minutes
extract.limit.time.limit=50
extract.limit.type=time
extract.namespace=gobblin.extract.kafka

simple.writer.delimiter=\n #recommended, JSON does not contain new lines
writer.builder.class=gobblin.writer.GZIPDataWriterBuilder
writer.destination.type=HDFS
writer.file.path.type=tablename
writer.include.record.count.in.file.names=true
writer.output.format=events.gz # this hack makes files compatible with camus, i.e. file.split('.')[3].to_i is event counter
writer.partition.pattern=yyyy/MM/dd/HH
writer.partition.timezone=UTC
writer.partitioner.class=gobblin.writer.partitioner.TimeBasedJsonWriterPartitioner

data.publisher.type=gobblin.publisher.TimePartitionedDataPublisher # required

mr.job.max.mappers=10

# where this are stored
fs.uri=hdfs://liquidm/
writer.fs.uri=hdfs://liquidm/
state.store.fs.uri=hdfs://liquidm/

# avoid races between concurrent gobblins (paranoid mode)
mr.job.root.dir=/gobblin/work/kafkacluster_topic
state.store.dir=/gobblin/state-store/kafkacluster_topic
task.data.root.dir=/gobblin/task/kafkacluster_topic
data.publisher.final.dir=/history/kafkacluster
```
