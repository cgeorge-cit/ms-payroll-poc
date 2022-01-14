package br.com.til.mspayroll

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MsPayrollApplication

fun main(args: Array<String>) {
	runApplication<MsPayrollApplication>(*args)
}
