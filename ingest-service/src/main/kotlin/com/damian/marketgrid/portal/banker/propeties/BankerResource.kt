package com.damian.marketgrid.portal.banker.propeties

import com.damian.marketgrid.model.ResourceKind

data class BankerResource(
    val kind: ResourceKind = ResourceKind.NONE,
    val namePl: String = "",
    val nameEng: String = ""
)
