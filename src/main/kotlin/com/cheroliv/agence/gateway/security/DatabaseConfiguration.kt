package com.cheroliv.agence.gateway.security

import com.cheroliv.agence.gateway.ProfileConstants
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.core.env.Environment
import org.springframework.data.convert.CustomConversions
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.DialectResolver
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.sql.SQLException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.jvm.Throws

@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
class DatabaseConfiguration(private val env: Environment) {
    private val log = LoggerFactory.getLogger(DatabaseConfiguration::class.java)

    /**
     * Open the TCP port for the H2 database, so it is available remotely.
     *
     * @return the H2 database TCP server.
     * @throws SQLException if the server failed to start.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile(ProfileConstants.SPRING_PROFILE_DEVELOPMENT)
    @Throws(SQLException::class)
    fun h2TCPServer(): Any {
        val port = validPortForH2
        log.debug("H2 database is available on port {}", port)
        return H2ConfigurationHelper.createServer(port)
    }

    //Integer.parseInt(env.getProperty("server.port"));
    private val validPortForH2: String
        get() {
            var port = 8082 //Integer.parseInt(env.getProperty("server.port"));
            port = if (port < 10000) {
                10000 + port
            } else {
                if (port < 63536) {
                    port + 2000
                } else {
                    port - 2000
                }
            }
            return port.toString()
        }

    // LocalDateTime seems to be the only type that is supported across all drivers atm
    // See https://github.com/r2dbc/r2dbc-h2/pull/139 https://github.com/mirromutth/r2dbc-mysql/issues/105
    @Bean
    fun r2dbcCustomConversions(connectionFactory: ConnectionFactory?): R2dbcCustomConversions {
        val dialect = DialectResolver.getDialect(connectionFactory!!)
        val converters: MutableList<Any> = ArrayList(dialect.converters)
        converters.add(InstantWriteConverter())
        converters.add(InstantReadConverter())
        converters.addAll(R2dbcCustomConversions.STORE_CONVERTERS)
        return R2dbcCustomConversions(
                CustomConversions.StoreConversions.of(dialect.simpleTypeHolder, converters), emptyList<Any>())
    }

    @WritingConverter
    class InstantWriteConverter : Converter<Instant?, LocalDateTime> {
        override fun convert(source: Instant): LocalDateTime {
            return LocalDateTime.ofInstant(source, ZoneOffset.UTC)
        }
    }

    @ReadingConverter
    class InstantReadConverter : Converter<LocalDateTime, Instant> {
        override fun convert(localDateTime: LocalDateTime): Instant {
            return localDateTime.toInstant(ZoneOffset.UTC)
        }
    }
}