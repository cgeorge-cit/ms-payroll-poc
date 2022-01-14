package br.com.til.mspayroll.adapters.restapi.dtos

import java.math.BigDecimal

data class PersonRequest(
        var id: String? = null,
        var cpf: String,
        var salary: BigDecimal?
        )
