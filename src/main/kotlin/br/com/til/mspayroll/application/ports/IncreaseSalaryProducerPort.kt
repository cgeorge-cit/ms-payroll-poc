package br.com.til.mspayroll.application.ports

import br.com.til.mspayroll.application.domains.Salary
import java.math.BigDecimal

interface IncreaseSalaryProducerPort {

    fun increaseSalaryProducer(salary: Salary)
}