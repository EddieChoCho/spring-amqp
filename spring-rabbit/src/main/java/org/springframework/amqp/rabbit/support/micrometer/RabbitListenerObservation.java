/*
 * Copyright 2022-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.amqp.rabbit.support.micrometer;

import java.util.Objects;

import io.micrometer.common.KeyValues;
import io.micrometer.common.docs.KeyName;
import io.micrometer.observation.Observation.Context;
import io.micrometer.observation.ObservationConvention;
import io.micrometer.observation.docs.ObservationDocumentation;

import org.springframework.amqp.core.MessageProperties;

/**
 * Spring Rabbit Observation for listeners.
 *
 * @author Gary Russell
 * @author Vincent Meunier
 * @author Artem Bilan
 * @author Ngoc Nhan
 *
 * @since 3.0
 */
public enum RabbitListenerObservation implements ObservationDocumentation {

	/**
	 * Observation for Rabbit listeners.
	 */
	LISTENER_OBSERVATION {

		@Override
		public Class<? extends ObservationConvention<? extends Context>> getDefaultConvention() {
			return DefaultRabbitListenerObservationConvention.class;
		}

		@Override
		public KeyName[] getLowCardinalityKeyNames() {
			return ListenerLowCardinalityTags.values();
		}

		@Override
		public KeyName[] getHighCardinalityKeyNames() {
			return ListenerHighCardinalityTags.values();
		}

	};

	/**
	 * Low cardinality tags.
	 */
	public enum ListenerLowCardinalityTags implements KeyName {

		/**
		 * Listener id.
		 */
		LISTENER_ID {

			@Override
			public String asString() {
				return "spring.rabbit.listener.id";
			}

		},

		/**
		 * The queue the listener is plugged to.
		 *
		 * @since 3.2
		 */
		DESTINATION_NAME {

			@Override
			public String asString() {
				return "messaging.destination.name";
			}

		}

	}

	/**
	 * High cardinality tags.
	 *
	 * @since 3.2.1
	 */
	public enum ListenerHighCardinalityTags implements KeyName {

		/**
		 * The delivery tag.
		 */
		DELIVERY_TAG {

			@Override
			public String asString() {
				return "messaging.rabbitmq.message.delivery_tag";
			}

		}

	}


	/**
	 * Default {@link RabbitListenerObservationConvention} for Rabbit listener key values.
	 */
	public static class DefaultRabbitListenerObservationConvention implements RabbitListenerObservationConvention {

		/**
		 * A singleton instance of the convention.
		 */
		public static final DefaultRabbitListenerObservationConvention INSTANCE =
				new DefaultRabbitListenerObservationConvention();

		@Override
		public KeyValues getLowCardinalityKeyValues(RabbitMessageReceiverContext context) {
			MessageProperties messageProperties = context.getCarrier().getMessageProperties();
			String consumerQueue = Objects.requireNonNullElse(messageProperties.getConsumerQueue(), "");
			return KeyValues.of(
					RabbitListenerObservation.ListenerLowCardinalityTags.LISTENER_ID.asString(),
					context.getListenerId(),
					RabbitListenerObservation.ListenerLowCardinalityTags.DESTINATION_NAME.asString(),
					consumerQueue);
		}

		@Override
		public KeyValues getHighCardinalityKeyValues(RabbitMessageReceiverContext context) {
			return KeyValues.of(RabbitListenerObservation.ListenerHighCardinalityTags.DELIVERY_TAG.asString(),
					String.valueOf(context.getCarrier().getMessageProperties().getDeliveryTag()));
		}

		@Override
		public String getContextualName(RabbitMessageReceiverContext context) {
			return context.getSource() + " receive";
		}

	}

}
