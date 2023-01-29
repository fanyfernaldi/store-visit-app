package com.fanyfernaldi.pitjarustest.responses

import com.fanyfernaldi.pitjarustest.domain.Store
import com.squareup.moshi.Json

data class LoginResponse(
    val stores: List<Store>,
    val status: String?,
    val message: String?,
)