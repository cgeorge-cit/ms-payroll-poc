package br.com.til.mspayroll.adapters.batch

import br.com.til.mspayroll.adapters.batch.itemreader.PayRollItemReader
import br.com.til.mspayroll.adapters.batch.processors.CalculatorFgtsItemProcessor
import br.com.til.mspayroll.adapters.batch.processors.GenerateReportFgtsItemProcessor
import br.com.til.mspayroll.adapters.kafka.producer.GenerateReportFgtsProducer
import br.com.til.mspayroll.adapters.repository.mongo.PersonRepository
import br.com.til.mspayroll.adapters.repository.mongo.documents.PersonPayRoll
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.core.io.FileSystemResource
import org.springframework.data.domain.Sort
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.math.BigDecimal
import java.util.*

@Configuration
@EnableBatchProcessing
@EnableScheduling
class BatchConfig(
        private val jobBuilderFactory: JobBuilderFactory,
        private val stepBuilderFactory: StepBuilderFactory,
        private val jobLauncher: JobLauncher,
        @Lazy private val personRepository: PersonRepository,
        @Lazy private val reportProducer: GenerateReportFgtsProducer
) {

    /*
    fun calculateFGTSStep(): Step {
        return stepBuilderFactory.get("calculateFGTSStep")
                .tasklet(Tasklet { stepContribution, chunkContext ->
                    val runTime = chunkContext.stepContext.jobParameters["runTime"].toString()
                    println("The run time step is: $runTime")
                    RepeatStatus.FINISHED
                }).build()
    }*/

    fun personPayRollItemReader(): ItemReader<PersonPayRoll> {
        return PayRollItemReader(personRepository)
    }

    fun personPayRollRepositoryItemReader(): ItemReader<PersonPayRoll> {
        val listArgs = listOf<BigDecimal>(BigDecimal.ONE)
        return RepositoryItemReaderBuilder<PersonPayRoll>()
                .repository(personRepository)
                .methodName("findByFgtsLessThan")
                .maxItemCount(10)
                .pageSize(10)
                .arguments(listArgs)
                .sorts(hashMapOf("cpf" to Sort.Direction.ASC))
                .name("repositoryItemReader")
                .build()
    }

    fun compositeItemProcesso(): ItemProcessor<PersonPayRoll, PersonPayRoll> {
        return CompositeItemProcessorBuilder<PersonPayRoll, PersonPayRoll>()
                .delegates(calculatorFgtsItemProcessor(), generateReportFgtsItemProcessor())
                .build()
    }

    fun generateReportFgtsItemProcessor(): ItemProcessor<PersonPayRoll, PersonPayRoll> {
        return GenerateReportFgtsItemProcessor(reportProducer)
    }

    fun calculatorFgtsItemProcessor(): ItemProcessor<PersonPayRoll, PersonPayRoll> {
        return CalculatorFgtsItemProcessor()
    }

    fun jsonFileItemWriter(): ItemWriter<PersonPayRoll> {
        return JsonFileItemWriterBuilder<PersonPayRoll>()
                .jsonObjectMarshaller(JacksonJsonObjectMarshaller())
                .resource(FileSystemResource("data/personPayRoll.json"))
                .name("jsonItemWriter")
                .build()
    }

    fun repositoryItemWriter() : ItemWriter<PersonPayRoll> {
        return RepositoryItemWriterBuilder<PersonPayRoll>()
                .repository(personRepository)
                .methodName("save")
                .build()
    }

    fun calculateFGTSStep(): Step {
        return stepBuilderFactory.get("calculateFGTSStep")
                .chunk<PersonPayRoll, PersonPayRoll>(1)
                .reader(personPayRollRepositoryItemReader())
                .processor(compositeItemProcesso())
                .writer(repositoryItemWriter())
                .build()
    }

    fun calculateFGTSJob(): Job {
        return jobBuilderFactory.get("calculateFGTSJob")
                .incrementer(RunIdIncrementer())
                .flow(calculateFGTSStep())
                .end()
                .build()
    }

    @Scheduled(cron = "0/20 * * * * *")
    fun runJob() {
        val paramBuilder = JobParametersBuilder()

        paramBuilder.addDate("runTime", Date())

        jobLauncher.run(calculateFGTSJob(), paramBuilder.toJobParameters())
    }
}