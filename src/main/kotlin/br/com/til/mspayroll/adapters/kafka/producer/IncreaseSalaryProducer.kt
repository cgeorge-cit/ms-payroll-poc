package br.com.til.mspayroll.adapters.kafka.producer

import br.com.til.mspayroll.application.domains.Salary
import br.com.til.mspayroll.application.ports.IncreaseSalaryProducerPort
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFutureCallback
import org.springframework.util.concurrent.SuccessCallback

@Service
class IncreaseSalaryProducer(
        private val kafkaTemplate: KafkaTemplate<String, String>
) : IncreaseSalaryProducerPort {

    override fun increaseSalaryProducer(salary: Salary) {
        println("Salary to producer: $salary")
        val salaryJson = ObjectMapper().writeValueAsString(salary)

        kafkaTemplate.send("payrollTopic", salaryJson)
    }
}
