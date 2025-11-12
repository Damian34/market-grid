package com.damian.marketgrid.portal

data class RawResource(
    val name: String,
    val price: Double,
    val currency: String,
    val unit: String?,
    val updatedAt: String
) {
    fun validate() {
        require(name.isNotBlank()) { "Invalid name of RawResource: $this" }
        require(price > 0) { "Invalid price of RawResource: $this" }
        require(currency.isNotBlank()) { "Invalid currency of RawResource: $this" }
        require(updatedAt.isNotBlank()) { "Invalid updatedAt of RawResource: $this" }
    }
}