package de.juplo.kafka;

import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.Duration;

import static de.juplo.kafka.KafkahandlerApplicationTests.TOPIC;


@SpringBootTest(
		properties = {
				"spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
				"spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
				"spring.kafka.producer.properties.spring.json.type.mapping=foo:de.juplo.kafka.Foo,bar:de.juplo.kafka.Bar"
		})
@EmbeddedKafka(
		bootstrapServersProperty = "spring.kafka.bootstrap-servers",
		topics = TOPIC)
class KafkahandlerApplicationTests
{
	static final String TOPIC = "test";

	@Autowired
	KafkaTemplate<String, ? super Object> kafkaTemplate;
	@Autowired
	MultiMessageConsumer consumer;


	@Test
	void receiveMessages()
	{
		Foo foo = new Foo();
		foo.setClient("peter");
		foo.setMessage("Hallo Welt!");
		kafkaTemplate.send(TOPIC, foo);

		Bar bar = new Bar();
		bar.setClient("ute");
		bar.setMessage("Greetings again!");
		kafkaTemplate.send(TOPIC, bar);

		Awaitility
				.await("Messages received")
				.atMost(Duration.ofSeconds(5))
				.untilAsserted(() ->
				{
					Assertions
							.assertThat(consumer.getFoos().size())
							.describedAs("All send foo-messages were received")
							.isEqualTo(1);
					Assertions
							.assertThat(consumer.getBars().size())
							.describedAs("All send bar-messages were received")
							.isEqualTo(1);
				});
	}
}
