package br.com.til.mspayroll.adapters.batch.itemreader

import br.com.til.mspayroll.adapters.repository.mongo.PersonRepository
import br.com.til.mspayroll.adapters.repository.mongo.documents.PersonPayRoll
import org.springframework.batch.item.ItemReader
import java.math.BigDecimal

class PayRollItemReader(
       private val personRepository: PersonRepository
) : ItemReader<PersonPayRoll> {

    override fun read(): PersonPayRoll? {

        val allPersons = personRepository.findAll().filter {
            it.fgts.compareTo(BigDecimal.ZERO) == 0
        }.toMutableList()

        println("List Size: ${allPersons.size}")

        return allPersons.removeFirstOrNull()

    }

}
