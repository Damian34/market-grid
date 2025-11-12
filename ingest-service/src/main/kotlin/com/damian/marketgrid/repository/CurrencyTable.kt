package com.damian.marketgrid.repository

import org.jooq.impl.DSL
import java.time.LocalDateTime

class CurrencyTable {
    companion object {
        val TABLE = DSL.table("currencies")
        val CODE = DSL.field("code", String::class.java)
        val NAME = DSL.field("name", String::class.java)
        val RATE  = DSL.field("rate", Double::class.java)
        val UPDATED_AT = DSL.field("updated_at", LocalDateTime::class.java)
    }
}