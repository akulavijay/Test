
package kafka;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.Logger;

public class Consumer {
	static Logger log = Logger.getLogger(Class.class.getName());

	public static void main(String[] args) {
		consume();
	}

	private static void consume() {
		System.out.println(">>consume()");
		Properties properties = new Properties();
		String topicName = "test-topic";
		properties.put("bootstrap.servers", "localhost:9092");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("group.id", "my-group");
		// properties.put("enable.auto.commit", "false");
		// properties.put("auto.offset.reset", "earliest");

		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
		List<String> topics = new ArrayList<String>();
		topics.add(topicName);
		kafkaConsumer.subscribe(topics);
		Collection<TopicPartition> topicPartitions = new ArrayList<TopicPartition>();
		PartitionInfo partitionInfo = kafkaConsumer.partitionsFor(topicName).get(0);
		topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
		TopicPartition partition;
		int eventsCount = 0;
		try {
			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
				for (ConsumerRecord<String, String> record : records) {
					partition = new TopicPartition(record.topic(), record.partition());
					System.out.println("Consumer" + (++eventsCount) + ":" + record.value() + ", Offset:"
							+ record.offset() + ", partition:" + partition);
				}
			}
		} catch (

		Exception e) {
			System.out.println(e.getMessage());
		} finally {
			kafkaConsumer.close();
		}
	}
}