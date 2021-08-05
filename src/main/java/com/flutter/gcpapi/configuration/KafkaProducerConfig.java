package com.flutter.gcpapi.configuration;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.security.scram.internals.ScramMechanism;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

import static java.util.Objects.isNull;

public class KafkaProducerConfig {

    private static final Properties getProperties(@Autowired KafkaProperties.Kafka kafkaConfig) {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaConfig.getBootstrapServers());

        if (kafkaConfig.getUseSSL()) {
            properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SSL.name());
        }

        final String authJaasConfig = kafkaConfig.getAuthJaasConfig();

        if (!isNull(authJaasConfig)) {
            properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_SSL.name());
            properties.setProperty(SaslConfigs.SASL_MECHANISM, ScramMechanism.SCRAM_SHA_512.mechanismName());
            properties.setProperty(SaslConfigs.SASL_JAAS_CONFIG, authJaasConfig);
        }
        return properties;
    }

}
