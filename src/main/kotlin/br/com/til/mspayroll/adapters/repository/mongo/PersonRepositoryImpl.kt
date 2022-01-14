package br.com.til.mspayroll.adapters.repository.mongo

import br.com.til.mspayroll.adapters.repository.mongo.documents.PersonPayRoll
import br.com.til.mspayroll.application.domains.PersonPayRollDTO
import br.com.til.mspayroll.application.ports.PersonRepositoryPort
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class PersonRepositoryImpl(
        @Lazy private var personRepository: PersonRepository
) : PersonRepositoryPort {

    override fun findPersonByCpf(cpf: String) : PersonPayRollDTO {

        val personDTO = personRepository.findByCpf(cpf).get().let {
            PersonPayRollDTO(
                    id = it.id,
                    cpf = it.cpf,
                    salary = it.salary
            )
        }
        println("Person Found: $personDTO")

        return personDTO
    }

    override fun save(person: PersonPayRollDTO) {
        personRepository.save(
                PersonPayRoll(id = person.id, cpf = person.cpf, salary=person.salary)
        )
    }
}