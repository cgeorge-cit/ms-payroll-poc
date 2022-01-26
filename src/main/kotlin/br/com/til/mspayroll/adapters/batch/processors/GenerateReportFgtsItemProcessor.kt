package br.com.til.mspayroll.adapters.batch.processors

import br.com.til.mspayroll.adapters.kafka.dtos.PayRollReportDTO
import br.com.til.mspayroll.adapters.kafka.producer.GenerateReportFgtsProducer
import br.com.til.mspayroll.adapters.repository.mongo.documents.PersonPayRoll
import org.springframework.batch.item.ItemProcessor

class GenerateReportFgtsItemProcessor(
        private val reporterProducer: GenerateReportFgtsProducer
) : ItemProcessor<PersonPayRoll, PersonPayRoll> {
    override fun process(person: PersonPayRoll): PersonPayRoll {

        reporterProducer.execute(PayRollReportDTO(
                cpf = person.cpf,
                fgts = person.fgts
        ))
        return person
    }

}
