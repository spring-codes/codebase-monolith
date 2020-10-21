package com.cheroliv.agence.gateway

import com.cheroliv.agence.gateway.GatewayAgenceApp.Companion.log
import com.cheroliv.agence.gateway.security.AuthoritiesConstants.ADMIN
import com.cheroliv.agence.gateway.security.AuthoritiesConstants.ANONYMOUS
import com.cheroliv.agence.gateway.security.AuthoritiesConstants.USER
import com.cheroliv.agence.gateway.security.Authority
import com.cheroliv.agence.gateway.security.AuthorityRepository
import com.cheroliv.agence.gateway.security.PersistenceAuditEventRepository
import com.cheroliv.agence.gateway.security.UserRepository
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator
import java.time.Duration


fun main(args: Array<String>) {
    log.info("Hi...")
    runApplication<GatewayAgenceApp>(*args)
}

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class GatewayAgenceApp {
    val greeting: String = "Hello world."

    companion object {
        @JvmStatic
        val log = LoggerFactory.getLogger(GatewayAgenceApp::class.java)
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
//            auditEventRepo: PersistenceAuditEventRepository,
    ): CommandLineRunner? = CommandLineRunner { demoProcess(authRepo) }

    fun demoProcess(authRepo: AuthorityRepository) {
        demoAuth(authRepo)
        /* TODO: userDemo(userRepo: UserRepository) and auditDemo(auditEventRepo: AuditEventRepository)""") */
    }

    fun demoAuth(authRepo: AuthorityRepository) {
        // save a few authorities
        authRepo.run {
            saveAll(listOf(
                    Authority(ANONYMOUS),
                    Authority(USER),
                    Authority(ADMIN)))
                    .blockLast(Duration.ofSeconds(10))
        }

        // fetch all customers
        log.info("authorities found with findAll():")
        log.info("-------------------------------")

        with(authRepo) {
            findAll().doOnNext { authority ->
                log.info(authority.toString())
            }.blockLast(Duration.ofSeconds(10))
        }
        log.info("")


        // fetch an individual authority by ID
        authRepo.findById(USER).doOnNext { authority ->
            log.info("authority found with findById('${USER}'):")
            log.info("--------------------------------")
            log.info(authority.toString())
            log.info("")
        }.block(Duration.ofSeconds(10))


        // fetch customers by last name
        log.info("authority found with findById('${ADMIN}'):")
        log.info("--------------------------------------------")
        authRepo.run {
            findById(ADMIN).doOnNext { admin: Authority? ->
                log.info(admin.toString())
            }.block(Duration.ofSeconds(10))
        }
        log.info("")
    }
}
