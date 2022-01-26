package br.com.til.mspayroll.adapters.kafka.dtos

import java.math.BigDecimal

data class PayRollReportDTO(
        val cpf: String,
        val fgts: BigDecimal
        )
