package br.com.til.mspayroll.adapters.repository.mongo

import br.com.til.mspayroll.adapters.repository.mongo.documents.PersonPayRoll
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*

@Repository
interface PersonRepository : PagingAndSortingRepository<PersonPayRoll, String> {

    fun findByCpf(cpf: String) : Optional<PersonPayRoll>

    fun findByFgtsLessThan(fgts: BigDecimal, pageable: Pageable) : Page<PersonPayRoll>
}