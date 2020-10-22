package com.cheroliv.codebase.monolith.security

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * An authority (a security role) used by Spring Security.
 */
@Table("authority")
class Authority(
        @Id var name: @NotNull @Size(max = 50) String? = null) :
        Serializable, Persistable<String?> {

    companion object {
        private const val serialVersionUID = 1L
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> {
                true
            }
            else -> {
                when (other) {
                    !is Authority -> {
                        false
                    }
                    else -> {
                        name == other.name
                    }
                }
            }
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(name)
    }

    // prettier-ignore
    override fun toString(): String {
        return "Authority{" +
                "name='" + name + '\'' +
                "}"
    }

    override fun getId(): String? {
        return name
    }

    override fun isNew(): Boolean {
        return true
    }
}