package de.juplo.kafka;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;


@Component
@KafkaListener(topics = "test")
@Slf4j
@Getter
public class MultiMessageConsumer
{
  private final List<Foo> foos = new LinkedList<>();
  private final List<Bar> bars = new LinkedList<>();


  @KafkaHandler
  public void handleFoo(
      @Payload Foo foo,
      @Header(KafkaHeaders.RECORD_METADATA) ConsumerRecordMetadata metadata)
  {
    log.info(
        "Received a Foo: {}, topic={}, partition={}, offset={}",
        foo,
        metadata.topic(),
        metadata.partition(),
        metadata.offset());
    foos.add(foo);
  }

  @KafkaHandler
  public void handleBar(
      @Payload Bar bar,
      @Header(KafkaHeaders.RECORD_METADATA) ConsumerRecordMetadata metadata)
  {
    log.info(
        "Received a Bar: {}",
        bar,
        metadata.topic(),
        metadata.partition(),
        metadata.offset());
    bars.add(bar);
  }

  @KafkaHandler(isDefault = true)
  void handleUnknown(
      @Payload Object unknown,
      @Header(KafkaHeaders.RECORD_METADATA) ConsumerRecordMetadata metadata)
  {
    log.info(
        "Received an unknown message: {}",
        unknown,
        metadata.topic(),
        metadata.partition(),
        metadata.offset());
  }
}
