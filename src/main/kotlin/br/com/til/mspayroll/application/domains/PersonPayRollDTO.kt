package br.com.til.mspayroll.application.domains

import java.math.BigDecimal

data class PersonPayRollDTO(
        val id: String? = null,
        val cpf: String,
        var salary: BigDecimal
)
