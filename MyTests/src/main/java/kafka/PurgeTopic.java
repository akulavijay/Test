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

public class PurgeTopic {
	 static Logger log = Logger.getLogger(Class.class.getName());  

	public static void main(String[] args) {

		log.debug(">>PurgeTopic.main()");
		Properties properties = new Properties();
		String topicName = "output-topic";
		properties.put("bootstrap.servers", "localhost:29092");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("group.id", "test-group");
		properties.put("enable.auto.commit", "false");
//		properties.put("auto.offset.reset", "earliest");

		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
		List<String> topics = new ArrayList<String>();
		topics.add(topicName);
		kafkaConsumer.subscribe(topics);
		Collection<TopicPartition> topicPartitions = new ArrayList<TopicPartition>();
		PartitionInfo partitionInfo = kafkaConsumer.partitionsFor(topicName).get(0);
		topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
		int eventsCount = 0;
		try {
			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
				for (ConsumerRecord<String, String> record : records) {
					log.debug("PurgeTopic" + (++eventsCount) + ":" + record.value());
					kafkaConsumer.commitSync();
				}
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
		} finally {
			kafkaConsumer.close();
		}
	}
}