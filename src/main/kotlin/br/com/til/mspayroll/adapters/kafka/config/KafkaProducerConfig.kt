package br.com.til.mspayroll.adapters.kafka.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

class KafkaProducerConfig {

    fun producerFactory(): ProducerFactory<String, String> = DefaultKafkaProducerFactory(producerConfigs())

    fun producerConfigs(): Map<String, Any> = mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "172.22.153.55:29092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        )

    fun kafkaTemplate(): KafkaTemplate<String, String>  = KafkaTemplate(producerFactory())

}