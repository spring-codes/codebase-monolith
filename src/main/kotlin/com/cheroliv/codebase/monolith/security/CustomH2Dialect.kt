package com.cheroliv.codebase.monolith.security

import org.hibernate.dialect.H2Dialect
import java.sql.Types

class CustomH2Dialect : H2Dialect() {
    init {
        registerColumnType(Types.FLOAT, "real")
    }
}