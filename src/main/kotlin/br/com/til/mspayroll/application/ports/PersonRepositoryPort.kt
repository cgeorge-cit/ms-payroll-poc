package br.com.til.mspayroll.application.ports

import br.com.til.mspayroll.application.domains.PersonPayRollDTO
import java.math.BigDecimal

interface PersonRepositoryPort {

    fun findPersonByCpf(cpf: String) : PersonPayRollDTO

    fun save(person: PersonPayRollDTO)
}