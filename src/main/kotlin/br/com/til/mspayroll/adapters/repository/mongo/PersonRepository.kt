package br.com.til.mspayroll.adapters.repository.mongo

import br.com.til.mspayroll.adapters.repository.mongo.documents.PersonPayRoll
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PersonRepository : MongoRepository<PersonPayRoll, String> {

    fun findByCpf(cpf: String) : Optional<PersonPayRoll>
}