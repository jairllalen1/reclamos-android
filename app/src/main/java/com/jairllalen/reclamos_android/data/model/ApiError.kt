package com.jairllalen.reclamos_android.data.model

data class ApiError(
    val timestamp: String? = null,
    val status: Int? = null,
    val error: String? = null,
    val message: String? = null,
    val errors: Map<String, String>? = null
)