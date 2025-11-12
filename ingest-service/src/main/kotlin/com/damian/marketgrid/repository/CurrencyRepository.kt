package com.damian.marketgrid.repository

import com.damian.marketgrid.model.Currency
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jooq.DSLContext

@ApplicationScoped
class CurrencyRepository(
    private val dsl: DSLContext
) {

    @Transactional
    fun saveOrUpdateAll(currencies: List<Currency>) {
        if (currencies.isEmpty()) return

        val codes = currencies.map { it.code }
        val existingCodes = dsl.select(CurrencyTable.CODE)
            .from(CurrencyTable.TABLE)
            .where(CurrencyTable.CODE.`in`(codes))
            .fetchSet(CurrencyTable.CODE)

        saveAll(currencies.filter { it.code !in existingCodes })
        updateAll(currencies.filter { it.code in existingCodes })
    }

    fun saveAll(currencies: List<Currency>) {
        if (currencies.isNotEmpty()) {
            dsl.insertInto(CurrencyTable.TABLE)
                .columns(
                    CurrencyTable.CODE,
                    CurrencyTable.NAME,
                    CurrencyTable.RATE,
                    CurrencyTable.UPDATED_AT
                )
                .also {
                    currencies.forEach { c ->
                        it.values(c.code, c.name, c.rate, c.updatedAt)
                    }
                }
                .execute()
        }
    }

    fun updateAll(currencies: List<Currency>) {
        currencies.forEach { c ->
            dsl.update(CurrencyTable.TABLE)
                .set(CurrencyTable.NAME, c.name)
                .set(CurrencyTable.RATE, c.rate)
                .set(CurrencyTable.UPDATED_AT, c.updatedAt)
                .where(CurrencyTable.CODE.eq(c.code))
                .execute()
        }
    }

    fun findByCodeIn(codes: Collection<String>): List<Currency> {
        if (codes.isEmpty()) return emptyList()

        return dsl.select(
            CurrencyTable.CODE,
            CurrencyTable.NAME,
            CurrencyTable.RATE,
            CurrencyTable.UPDATED_AT
        )
            .from(CurrencyTable.TABLE)
            .where(CurrencyTable.CODE.`in`(codes))
            .fetch { record ->
                Currency(
                    code = record[CurrencyTable.CODE],
                    name = record[CurrencyTable.NAME],
                    rate = record[CurrencyTable.RATE],
                    updatedAt = record[CurrencyTable.UPDATED_AT]
                )
            }
    }

    fun getAll(): List<Currency> {
        return dsl.select(
            CurrencyTable.CODE,
            CurrencyTable.NAME,
            CurrencyTable.RATE,
            CurrencyTable.UPDATED_AT
        )
            .from(CurrencyTable.TABLE)
            .fetch { record ->
                Currency(
                    code = record[CurrencyTable.CODE],
                    name = record[CurrencyTable.NAME],
                    rate = record[CurrencyTable.RATE],
                    updatedAt = record[CurrencyTable.UPDATED_AT]
                )
            }
    }
}
