package br.com.til.mspayroll.adapters.kafka.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaConfig {

    @Bean
    fun admin() = KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to "172.24.237.244:29092"))

    @Bean
    fun payrollTopic() = TopicBuilder.name("payrollTopic").build()

    @Bean
    fun reporterTopic() = TopicBuilder.name("reporterTopic").build()

}