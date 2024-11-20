package co.edu.javeriana.msc.turismo.notification.queue;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import co.edu.javeriana.msc.turismo.notification.dtos.Customer;
import co.edu.javeriana.msc.turismo.notification.dtos.PurchaseNotification;
import co.edu.javeriana.msc.turismo.notification.dtos.SuperService;
import co.edu.javeriana.msc.turismo.notification.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.notification.enums.Status;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
class MessageQueueConsumerTest {

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.6");

    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongo::getHost);
        registry.add("spring.data.mongodb.port", mongo::getFirstMappedPort);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    private static KafkaTemplate<String, Object> kafkaTemplate;

    static KafkaConsumer<Object, Object> mockKafkaConsumer;

    static void createMockKafkaConsumer() {
        String groupId = "order-notification-group";
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "co.edu.javeriana.msc.*");
        mockKafkaConsumer = new KafkaConsumer<>(properties, new JsonDeserializer<>(), new JsonDeserializer<>());
        mockKafkaConsumer.subscribe(List.of("contentsQueue"));
    }

    static void createKafkaProducer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);

        configProps.put(ProducerConfig.ACKS_CONFIG, "1");

        ProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }


    static final PurchaseNotification notification = new PurchaseNotification(
            "NOTIF123456",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            Status.ACEPTADA,
            PaymentStatus.ACEPTADA,
            null,
            BigDecimal.valueOf(600.00)
    );

    @BeforeAll
    static void setup() {
        mongo.start();
        kafka.start();
        createMockKafkaConsumer();
        createKafkaProducer();
        Gson gson = new Gson();
        kafkaTemplate.send("order-notification-queue", gson.toJson(notification));
    }

    @AfterAll
    static void teardown() {
        kafka.stop();
        mongo.stop();
    }

    @Test
    @Order(1)
    void receiveNotification() {
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            mockKafkaConsumer.subscribe(List.of("order-notification-queue"));
            var records = mockKafkaConsumer.poll(Duration.ofSeconds(5));
            assertThat(records).isNotEmpty();
            var record = records.iterator().next();
            var receivedNotification = (PurchaseNotification) record.value();
            assertThat(receivedNotification).isEqualTo(notification);
        });
    }
}