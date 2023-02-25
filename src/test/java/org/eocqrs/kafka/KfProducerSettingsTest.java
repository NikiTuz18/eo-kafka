/*
 *  Copyright (c) 2022 Aliaksei Bialiauski, EO-CQRS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.eocqrs.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcabi.xml.XMLDocument;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.eocqrs.kafka.producer.KfProducerSettings;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link KfProducerSettings}
 *
 * @since 0.0.0
 */
class KfProducerSettingsTest {

  @Test
  void testProducerConstruction() throws FileNotFoundException {
    final ProducerSettings<String, String> settings =
      new KfProducerSettings<>(
        new XMLDocument(
          new File("src/test/resources/settings.xml")
        )
      );
    final KafkaProducer<String, String> out = settings.producer();
    assertThat(out).isNotNull();
  }
}