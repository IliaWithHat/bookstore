package org.ilia.bookstore.integration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;

import java.util.Optional;

public abstract class GrpcServerTestBase extends IntegrationTestBase {

    protected ManagedChannel channel;

    @LocalRunningGrpcPort
    protected int runningPort;

    @BeforeEach
    public void setupChannels() {
        channel = ManagedChannelBuilder.forAddress("localhost", runningPort)
                .usePlaintext()
                .build();
    }

    @AfterEach
    public void shutdownChannels() {
        Optional.ofNullable(channel).ifPresent(ManagedChannel::shutdownNow);
    }
}
