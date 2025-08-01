/*
 * Copyright 2021-present the original author or authors.
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

package org.springframework.amqp.rabbit.connection;

import java.io.Serial;

import org.springframework.amqp.AmqpException;

/**
 * Represents a failure to commit or rollback when performing afterCompletion
 * after the primary transaction completes.
 *
 * @author Gary Russell
 * @since 2.4
 */
public class AfterCompletionFailedException extends AmqpException {

	@Serial
	private static final long serialVersionUID = 1L;

	private final int syncStatus;

	/**
	 * Construct an instance with the provided properties.
	 * @param syncStatus the synchronization status.
	 * @param cause the cause.
	 */
	public AfterCompletionFailedException(int syncStatus, Throwable cause) {
		super(cause);
		this.syncStatus = syncStatus;
	}

	/**
	 * Return the synchronization status.
	 * @return the status.
	 */
	public int getSyncStatus() {
		return this.syncStatus;
	}

}
