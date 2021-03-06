import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.time.Duration;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();

        // kafka boostrap server
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", "test");
//        properties.setProperty("enable.auto.commit", "true"); //true by default
        properties.setProperty("auto.commit.intervals.ms", "1000");
        properties.setProperty("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        kafkaConsumer.subscribe(Arrays.asList("second_topic"));

        while(true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(10));
            for(ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println("Partition: " + consumerRecord.partition() + ", " +
                        "Offset: " + consumerRecord.offset() + ", " +
                        "Key: " + consumerRecord.key() + ", " +
                        "Value: " + consumerRecord.value());
            }
            kafkaConsumer.commitAsync(); //used to control offset commits
        }
    }
}
