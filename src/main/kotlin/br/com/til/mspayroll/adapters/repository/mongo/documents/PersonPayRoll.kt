package br.com.til.mspayroll.adapters.repository.mongo.documents

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document("personPayRoll")
class PersonPayRoll(
    var id: String? = null,
    var cpf: String,
    var salary: BigDecimal
)