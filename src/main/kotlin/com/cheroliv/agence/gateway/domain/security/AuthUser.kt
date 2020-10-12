package com.cheroliv.agence.gateway.domain.security

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

        @JsonIgnore @Column("password_hash") var password: @NotNull @Size(min = 60, max = 60) String? = null,
        @Column("first_name") var firstName: @Size(max = 50) String? = null,
        @Column("last_name") var lastName: @Size(max = 50) String? = null,
        var email: @Email @Size(min = 5, max = 254) String? = null,

        var activated: Boolean = false,
        @Column("lang_key") var langKey: @Size(min = 2, max = 10) String? = null,
        @Column("image_url") var imageUrl: @Size(max = 256) String? = null,
        @Column("activation_key") @JsonIgnore var activationKey: @Size(max = 20) String? = null,
        @Column("reset_key") @JsonIgnore var resetKey: @Size(max = 20) String? = null,
        @Column("reset_date") var resetDate: Instant? = null,
        @JsonIgnore @Transient var authorities: Set<Authority> = HashSet())
    : AbstractAuditingEntity(), Serializable {
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

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        return if (o !is AuthUser) {
            false
        } else id != null && id == o.id
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