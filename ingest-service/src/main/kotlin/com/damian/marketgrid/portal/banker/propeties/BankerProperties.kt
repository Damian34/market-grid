package com.damian.marketgrid.portal.banker.propeties

data class BankerProperties(
    val resources: Map<String, BankerResource>,
    val units: Map<String, BankerUnitMapping>
)
