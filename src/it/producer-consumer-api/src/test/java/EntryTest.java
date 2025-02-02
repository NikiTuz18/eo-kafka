/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 Aliaksei Bialiauski, EO-CQRS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import io.github.eocqrs.kafka.Consumer;
import io.github.eocqrs.kafka.Producer;
import io.github.eocqrs.kafka.consumer.KfConsumer;
import io.github.eocqrs.kafka.consumer.settings.KfConsumerParams;
import io.github.eocqrs.kafka.data.KfData;
import io.github.eocqrs.kafka.parameters.BootstrapServers;
import io.github.eocqrs.kafka.parameters.GroupId;
import io.github.eocqrs.kafka.parameters.KeyDeserializer;
import io.github.eocqrs.kafka.parameters.KeySerializer;
import io.github.eocqrs.kafka.parameters.KfFlexible;
import io.github.eocqrs.kafka.parameters.KfParams;
import io.github.eocqrs.kafka.parameters.ValueDeserializer;
import io.github.eocqrs.kafka.parameters.ValueSerializer;
import io.github.eocqrs.kafka.producer.KfProducer;
import io.github.eocqrs.kafka.producer.settings.KfProducerParams;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * @todo #81 Tests to produce-consume data.
 * Write a test which will be check how consumer
 * reads data from producer.
 */
/**
 * Entry test cases.
 *
 * @author Ivan Ivanchuk (l3r8y@duck.com)
 * @since 0.0.2
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
final class EntryTest {

  @Container
  private static final KafkaContainer KAFKA = new KafkaContainer(
    DockerImageName.parse("confluentinc/cp-kafka:7.3.0")
  )
    .withEnv("auto.create.topics.enable", "true")
    .withEmbeddedZookeeper();

  private final String severs =
    EntryTest.KAFKA.getBootstrapServers().replace("PLAINTEXT://", "");

  @Test
  @Order(1)
  void runsKafka() {
    MatcherAssert.assertThat(
      "Container runs",
      EntryTest.KAFKA.isRunning(),
      Matchers.is(true)
    );
  }

  @Test
  @Order(2)
  void createsConsumerAndSubscribes() throws Exception {
    try (
      final Consumer<String, String> consumer =
        new KfConsumer<>(
          new KfFlexible<>(
            new KfConsumerParams(
              new KfParams(
                new BootstrapServers(this.severs),
                new GroupId("1"),
                new KeyDeserializer("org.apache.kafka.common.serialization.StringDeserializer"),
                new ValueDeserializer("org.apache.kafka.common.serialization.StringDeserializer")
              )
            )
          )
        )
      ) {
      Assertions.assertDoesNotThrow(() -> consumer.subscribe(new ListOf<>("fake")));
    }
  }

  @Test
  @Order(3)
  void createsProducerAndSendsData() throws Exception {
    try (
      final Producer<String, String> producer =
        new KfProducer<>(
          new KfFlexible<>(
            new KfProducerParams(
              new KfParams(
                new BootstrapServers(this.severs),
                new KeySerializer("org.apache.kafka.common.serialization.StringSerializer"),
                new ValueSerializer("org.apache.kafka.common.serialization.StringSerializer")
              )
            )
          )
        )
    ) {
      Assertions.assertDoesNotThrow(
        () -> producer.send(
          "fake-key",
          new KfData<>("fake-data", "101", 1)
        )
      );
    }
  }
}
