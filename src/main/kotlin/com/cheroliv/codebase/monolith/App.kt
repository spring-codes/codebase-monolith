package com.cheroliv.codebase.monolith

import com.cheroliv.codebase.monolith.App.Companion.log
import com.cheroliv.codebase.monolith.security.AuthoritiesConstants.ADMIN
import com.cheroliv.codebase.monolith.security.AuthoritiesConstants.ANONYMOUS
import com.cheroliv.codebase.monolith.security.AuthoritiesConstants.USER
import com.cheroliv.codebase.monolith.security.Authority
import com.cheroliv.codebase.monolith.security.AuthorityRepository
import com.cheroliv.codebase.monolith.security.PersistenceAuditEventRepository
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator
import java.time.Duration.ofSeconds


fun main(args: Array<String>) {
    log.info("Hi...")
    runApplication<App>(*args)
}

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class App {
    val greeting: String = "Hello world."

    companion object {
        @JvmStatic
        val log: Logger by lazy { getLogger(App::class.java) }
    }


    @Bean
    fun initializer(
            @Qualifier("connectionFactory")
            connectionFactory: ConnectionFactory?,
    ): ConnectionFactoryInitializer? = ConnectionFactoryInitializer().apply {
        setConnectionFactory(connectionFactory!!)
        setDatabasePopulator(ResourceDatabasePopulator(
                ClassPathResource("schema.sql")))
    }

    @Bean
    fun demo(
            authRepo: AuthorityRepository,
//            userRepo: UserRepository,
            auditEventRepo: PersistenceAuditEventRepository,
    ): CommandLineRunner? = CommandLineRunner {
        demoProcess(
                authRepo,
//            userRepo,
                auditEventRepo)
    }

    fun demoProcess(
            authRepo: AuthorityRepository,
//            userRepo: UserRepository,
            auditEventRepo: PersistenceAuditEventRepository,
    ) {
        demoAuth(authRepo)
//        userDemo(userRepo)
        auditDemo(auditEventRepo)
    }

//    fun userDemo(userRepo: UserRepository) {
//        log.info("wassup wassup!")
//    }

    fun auditDemo(auditEventRepo: PersistenceAuditEventRepository) {
        log.info("wassup wassup!")
        log.info("auditEventRepo.count() = ${auditEventRepo.count()}")
    }

    fun demoAuth(authRepo: AuthorityRepository) {
        // save a few authorities
        authRepo.run {
            saveAll(listOf(
                    Authority(ANONYMOUS),
                    Authority(USER),
                    Authority(ADMIN)))
                    .blockLast(ofSeconds(10))
        }

        // fetch all customers
        log.info("authorities found with findAll():")
        log.info("-------------------------------")

        with(authRepo) {
            findAll().doOnNext { authority ->
                log.info(authority.toString())
            }.blockLast(ofSeconds(10))
        }
        log.info("")


        // fetch an individual authority by ID
        authRepo.findById(USER).doOnNext { authority ->
            log.info("authority found with findById('${USER}'):")
            log.info("--------------------------------")
            log.info(authority.toString())
            log.info("")
        }.block(ofSeconds(10))


        // fetch customers by last name
        log.info("authority found with findById('${ADMIN}'):")
        log.info("--------------------------------------------")
        authRepo.run {
            findById(ADMIN).doOnNext { admin: Authority? ->
                log.info(admin.toString())
            }.block(ofSeconds(10))
        }
        log.info("")
    }
}
