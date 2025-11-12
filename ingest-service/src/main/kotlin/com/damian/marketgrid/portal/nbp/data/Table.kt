package com.damian.marketgrid.portal.nbp.data

data class Table(
    val table: String,
    val no: String,
    val effectiveDate: String,
    val rates: List<Rate>
)