package kafka;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

public class PauseAndResume {

	public static void main(String[] args) {
		int retryDelay = 5; // seconds
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Producer.produce(4);
		Properties properties = new Properties();
		String topicName = "output-topic";
		properties.put("bootstrap.servers", "localhost:29092");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("group.id", "test-group");
		properties.put("max.poll.records", 1);
		properties.put("enable.auto.commit", false);

		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
		List<String> topics = new ArrayList<String>();
		topics.add(topicName);
		kafkaConsumer.subscribe(topics);
		Collection<TopicPartition> topicPartitions = new ArrayList<TopicPartition>();
		PartitionInfo partitionInfo = kafkaConsumer.partitionsFor(topicName).get(0);
		topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
		int retries = 0;
		boolean resetRetry = false;
		try {
			Date pausedAt = new Date();
			while (true) {
				if (resetRetry)
					retries = 0;
				if (!kafkaConsumer.paused().isEmpty()) {
					if ((new Date().getTime() - pausedAt.getTime()) / 1000 % 60 >= retryDelay) {
						System.out.println("Resuming Consumer..." + sdf.format(new Date()));
						kafkaConsumer.resume(topicPartitions);
					}else {
						continue;
					}
				}
				ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
				for (ConsumerRecord<String, String> record : records) {
					System.out.println("Polled Event String:" + record.value());
					if (record.value().equals("Test Event - 2") && retries <= 2) {
						System.out.println(
								"Due to system error, the Consumer is pausing for about " + retryDelay + " seconds from "
										+ sdf.format(new Date()) + ", on topic " + topicPartitions.iterator().next());
						kafkaConsumer.pause(topicPartitions);
						kafkaConsumer.seek(topicPartitions.iterator().next(), record.offset());
						pausedAt = new Date();
						retries++;
						resetRetry = false;
						break;
					} else {
						kafkaConsumer.commitSync();
						resetRetry = true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			kafkaConsumer.close();
		}
	}

}
