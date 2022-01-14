package br.com.til.mspayroll.application.core

import br.com.til.mspayroll.application.domains.PersonPayRollDTO
import br.com.til.mspayroll.application.domains.Salary
import br.com.til.mspayroll.application.ports.IncreaseSalaryProducerPort
import br.com.til.mspayroll.application.ports.PersonRepositoryPort
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

class IncreaseSalaryServiceImpl(
        val personRepositoryPort: PersonRepositoryPort,
        val increaseSalaryProducerPort: IncreaseSalaryProducerPort
) : IncreaseSalaryService {

    override fun increaseSalary(cpf: String) {

        val newPersonSalary = personRepositoryPort.findPersonByCpf(cpf)
                .apply {
                    salary = salary.plus(
                            salary.multiply(BigDecimal(0.1))
                    ).setScale(2, RoundingMode.HALF_UP)
            }.let {
                personRepositoryPort.save(
                        PersonPayRollDTO(
                                id = it.id,
                                cpf = it.cpf,
                                salary = it.salary
                        )
                )
                it.salary
            }

        println("New Salary for $cpf: $newPersonSalary")
        increaseSalaryProducerPort.increaseSalaryProducer(
                Salary(cpf, newPersonSalary)
        )

    }
}
