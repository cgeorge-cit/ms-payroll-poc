package br.com.til.mspayroll.adapters.batch.processors

import br.com.til.mspayroll.adapters.repository.mongo.documents.PersonPayRoll
import org.springframework.batch.item.ItemProcessor
import java.math.BigDecimal

class CalculatorFgtsItemProcessor : ItemProcessor<PersonPayRoll, PersonPayRoll> {

    override fun process(person: PersonPayRoll) = person.also {
            it.fgts = it.salary.multiply(BigDecimal("0.08"))
        }

}
