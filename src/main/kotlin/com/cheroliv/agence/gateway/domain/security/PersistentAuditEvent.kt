package com.cheroliv.agence.gateway.domain.security

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.Instant
import java.util.*
import javax.validation.constraints.NotNull

/**
 * Persist AuditEvent managed by the Spring Boot actuator.
 *
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
@Table("persistent_audit_event")
class PersistentAuditEvent(
        @Id
        @Column("event_id")
        var id: Long? = null,
        @NotNull
        var principal: String? = null,
        @Column("event_date")
        var auditEventDate: Instant? = null,
        @Column("event_type")
        var auditEventType: String? = null,
        @Transient
        var data: Map<String, String> = HashMap())
    : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is PersistentAuditEvent) {
            false
        } else id != null && id == other.id
    }

    override fun hashCode(): Int {
        return 31
    }

    // prettier-ignore
    override fun toString(): String {
        return "PersistentAuditEvent{" +
                "principal='" + principal + '\'' +
                ", auditEventDate=" + auditEventDate +
                ", auditEventType='" + auditEventType + '\'' +
                '}'
    }
}