package david.hababou.sandbox.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import david.hababou.sandbox.model.SandBoxElement;

@Configuration
public class SandBoxKafkaProducerConfiguration {
	
	@Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;
	@Value(value = "${kafka.topic1}")
	private String topic;
	@Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
    
    @Bean
    public NewTopic topic1() {
         return new NewTopic(topic, 1, (short) 1);
    }
    
    @Bean
    public ProducerFactory<String, SandBoxElement> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
          bootstrapAddress);
        configProps.put(
          ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
          StringSerializer.class);
        configProps.put(
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
          JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, SandBoxElement> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public KafkaAdminClient kfkAdmClnt() {
      Map<String, Object> props = new HashMap<>();
      props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
      return (KafkaAdminClient)AdminClient.create(props);
    }
}