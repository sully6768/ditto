/*
 * Copyright (c) 2017-2018 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.services.connectivity.messaging.kafka;

import static java.util.Collections.singletonList;
import static org.eclipse.ditto.services.connectivity.messaging.TestConstants.Authorization.AUTHORIZATION_CONTEXT;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.assertj.core.api.Assertions;
import org.eclipse.ditto.model.base.headers.DittoHeaders;
import org.eclipse.ditto.model.connectivity.Connection;
import org.eclipse.ditto.model.connectivity.ConnectionConfigurationInvalidException;
import org.eclipse.ditto.model.connectivity.ConnectionType;
import org.eclipse.ditto.model.connectivity.ConnectivityModelFactory;
import org.eclipse.ditto.model.connectivity.ConnectivityStatus;
import org.eclipse.ditto.model.connectivity.Source;
import org.eclipse.ditto.model.connectivity.Topic;
import org.junit.Test;

/**
 * Unit test for {@link org.eclipse.ditto.services.connectivity.messaging.kafka.KafkaValidator}.
 */
public class KafkaValidatorTest {

    @Test
    public void testSourcesAreInvalid() {
        verifyConnectionConfigurationInvalidExceptionIsThrownForSource(source("any"));
    }

    private Source source(final String address) {
        return ConnectivityModelFactory.newSource(AUTHORIZATION_CONTEXT, address);
    }

    private void verifyConnectionConfigurationInvalidExceptionIsThrownForSource(final Source source) {
        Assertions.assertThatExceptionOfType(ConnectionConfigurationInvalidException.class)
                .isThrownBy(() -> KafkaValidator.newInstance().validateSource(source, DittoHeaders.empty(), () -> ""));
    }

    @Test
    public void testValidTargetAddress() {
        KafkaValidator.newInstance().validate(connectionWithTarget("events"), DittoHeaders.empty());
        KafkaValidator.newInstance().validate(connectionWithTarget("events/"), DittoHeaders.empty());
        KafkaValidator.newInstance().validate(connectionWithTarget("ditto#"), DittoHeaders.empty());
        KafkaValidator.newInstance().validate(connectionWithTarget("ditto/{{thing:id}}"), DittoHeaders.empty());
        KafkaValidator.newInstance().validate(connectionWithTarget("events#{{topic:full}}"), DittoHeaders.empty());
        KafkaValidator.newInstance().validate(connectionWithTarget("ditto/{{header:x}}"), DittoHeaders.empty());
    }

    @Test
    public void testInvalidTargetAddress() {
        verifyConnectionConfigurationInvalidExceptionIsThrown(connectionWithTarget(""));
        verifyConnectionConfigurationInvalidExceptionIsThrown(connectionWithTarget("ditto*a"));
        verifyConnectionConfigurationInvalidExceptionIsThrown(connectionWithTarget("ditto\\"));
    }

    private Connection connectionWithTarget(final String target) {
        return ConnectivityModelFactory.newConnectionBuilder("kafka", ConnectionType.KAFKA,
                ConnectivityStatus.OPEN, "tcp://localhost:1883")
                .targets(singletonList(
                        ConnectivityModelFactory.newTarget(target, AUTHORIZATION_CONTEXT, null, 1, Topic.LIVE_EVENTS)))
                .build();
    }


    private void verifyConnectionConfigurationInvalidExceptionIsThrown(final Connection connection) {
        Assertions.assertThatExceptionOfType(ConnectionConfigurationInvalidException.class)
                .isThrownBy(() -> KafkaValidator.newInstance().validate(connection, DittoHeaders.empty()));
    }


    @Test
    public void testImmutability() {
        assertInstancesOf(KafkaValidator.class, areImmutable());
    }
}