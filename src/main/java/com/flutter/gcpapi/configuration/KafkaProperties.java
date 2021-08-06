package com.flutter.gcpapi.configuration;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Loads all TLA configuration from application.yaml
 */
@ConfigurationProperties(prefix = "gcpapi")
@Component(value = "gcpapiProperties")
@Getter
@Setter
public class KafkaProperties {

    /**
     * Kafka details
     */
    private Kafka kafka;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Kafka {
        /**
         * Kafka bootstrap servers
         */
        private String bootstrapServers;

        /**
         * Trader Comments topic name
         */
        private String topicTraderComments;

        /**
         * Time to wait for a response from the cluster description operation.
         */
        private Duration responseTimeout;

        /**
         * Frequency of cluster description operation.
         */
        private Duration checkFrequency;

        /**
         * Use SSL Protocol to communicate with brokers.
         */
        private Boolean useSSL;

        /**
         * Config String to connect with Authentication.
         */
        private String authJaasConfig;
    }

}
