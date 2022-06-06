package de.juplo.kafka;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
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
  public void handleFoo(Foo foo)
  {
    log.info("Received a Foo: {}", foo);
    foos.add(foo);
  }

  @KafkaHandler
  public void handleBar(Bar bar)
  {
    log.info("Received a Bar: {}", bar);
    bars.add(bar);
  }
}
