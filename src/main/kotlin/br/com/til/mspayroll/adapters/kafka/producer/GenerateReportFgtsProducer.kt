package br.com.til.mspayroll.adapters.kafka.producer

import br.com.til.mspayroll.adapters.kafka.dtos.PayRollReportDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class GenerateReportFgtsProducer(
        private val kafkaTemplate: KafkaTemplate<String, String>
) {

    fun execute(reportDTO: PayRollReportDTO) {
        println("FGTS to producer: $reportDTO")
        val reportJson = ObjectMapper().writeValueAsString(reportDTO)

        kafkaTemplate.send("reportTopic", reportJson)
    }
}