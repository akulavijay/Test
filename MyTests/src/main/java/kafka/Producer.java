package kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

public class Producer {
	static Logger log = Logger.getLogger(Class.class.getName());

	public static void produce(int eventsSize) {

		log.info(">>Producer.produce()");
		log.error(">>Producer.produce()");
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");
		//properties.put("bootstrap.servers", "34.125.51.128:9092"); //Google Cloud VM
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		int eventsPublished = 0;
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
		try {
			for (int i = 0; i < eventsSize; i++) {
				kafkaProducer.send(new ProducerRecord<String, String>("test-topic", Integer.toString(i), "{\"Test\": \"Test\"}"));
				eventsPublished++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			kafkaProducer.close();
		}
		System.out.println("<<Producer.produce() " + eventsPublished + " events publised");
	}

	public static void main(String[] args) {
		produce(5);
	}
}