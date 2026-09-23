package com.jairllalen.reclamos_android.data.model

data class EntityTypeUpdateRequest(
    val nameTypeEntity: String,
    val entitySize: String,
    val sector: String,
    val idUserUpdate: Int
)