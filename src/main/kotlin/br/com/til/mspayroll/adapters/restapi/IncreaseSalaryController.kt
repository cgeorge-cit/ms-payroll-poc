package br.com.til.mspayroll.adapters.restapi

import br.com.til.mspayroll.adapters.kafka.producer.IncreaseSalaryProducer
import br.com.til.mspayroll.adapters.repository.mongo.PersonRepositoryImpl
import br.com.til.mspayroll.adapters.restapi.dtos.PersonRequest
import br.com.til.mspayroll.application.core.IncreaseSalaryService
import br.com.til.mspayroll.application.core.IncreaseSalaryServiceImpl
import br.com.til.mspayroll.application.domains.PersonPayRollDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.math.RoundingMode

@RestController
@RequestMapping(("/increase-salary"))
class IncreaseSalaryController(
        private val personRepositoryImpl: PersonRepositoryImpl,
        private val increaseSalaryProducer: IncreaseSalaryProducer
) {

    val increaseSalaryService: IncreaseSalaryService = IncreaseSalaryServiceImpl(personRepositoryImpl, increaseSalaryProducer)

    @PostMapping
    fun increaseSalary(@RequestBody requestDTO: PersonRequest) {
        println("Person CPF: ${requestDTO.cpf}")
        increaseSalaryService.increaseSalary(requestDTO.cpf)
    }

    @PostMapping("/save")
    fun save(@RequestBody requestDTO: PersonRequest) {
        println("CPF: ${requestDTO.cpf}")
        println("Salary to save: ${requestDTO.salary}")
        requestDTO.let {

            personRepositoryImpl.save(PersonPayRollDTO(
                    cpf = it.cpf,
                    salary = it.salary ?: BigDecimal.ZERO
            ))
        }
    }

    @GetMapping
    fun calculateIncrease(@RequestParam salary:String) : ResponseEntity<String> {

        val newSalary = BigDecimal(salary).plus(
                BigDecimal(salary).multiply(BigDecimal(0.1))
        ).setScale(2, RoundingMode.HALF_UP).toString()
        return ResponseEntity.ok().body(newSalary)
    }

}