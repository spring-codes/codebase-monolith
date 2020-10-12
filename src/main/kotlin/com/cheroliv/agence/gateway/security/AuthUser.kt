package com.cheroliv.agence.gateway.security

import com.cheroliv.agence.gateway.Constants
import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.commons.lang3.StringUtils
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.Instant
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * A user.
 */
@Table("user")
class AuthUser(
        @Id
        var id: Long? = null,

        @NotNull
        @Pattern(regexp = Constants.LOGIN_REGEX)
        @Size(min = 1, max = 50)
        private var login: String? = null,

        @JsonIgnore
        @Column("password_hash")
        @NotNull
        @Size(min = 60, max = 60)
        var password: String? = null,

        @Column("first_name")
        @Size(max = 50)
        var firstName: String? = null,

        @Column("last_name")
        @Size(max = 50)
        var lastName: String? = null,

        @Email
        @Size(min = 5, max = 254)
        var email: String? = null,

        var activated: Boolean = false,

        @Column("lang_key")
        @Size(min = 2, max = 10)
        var langKey: String? = null,

        @Column("image_url")
        @Size(max = 256)
        var imageUrl: String? = null,

        @Column("activation_key")
        @JsonIgnore
        @Size(max = 20)
        var activationKey: String? = null,

        @Column("reset_key")
        @JsonIgnore
        @Size(max = 20)
        var resetKey: String? = null,

        @Column("reset_date")
        var resetDate: Instant? = null,

        @JsonIgnore
        @Transient
        var authorities: Set<Authority> = HashSet()

) : AbstractAuditingEntity(), Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    fun getLogin(): String? {
        return login
    }

    // Lowercase the login before saving it in database
    fun setLogin(login: String?) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other !is AuthUser) {
            false
        } else id != null && id == other.id
    }

    override fun hashCode(): Int {
        return 31
    }

    // prettier-ignore
    override fun toString(): String {
        return "User{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", activated='" + activated + '\'' +
                ", langKey='" + langKey + '\'' +
                ", activationKey='" + activationKey + '\'' +
                "}"
    }


}