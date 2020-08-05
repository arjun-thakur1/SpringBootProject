package work1.project1.package1.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaConfiguration {

/*
    @Bean
    public ConsumerFactory<? super String, ? super KafkaRequestConsume> requestEmployeeEntityConsumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(KafkaRequestConsume.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaRequestConsume> employeeEntityKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaRequestConsume> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(requestEmployeeEntityConsumerFactory());
        return factory;
    }
*/

}
