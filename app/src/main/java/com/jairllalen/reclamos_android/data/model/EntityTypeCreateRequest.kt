package com.jairllalen.reclamos_android.data.model

data class EntityTypeCreateRequest(
    val nameTypeEntity: String,
    val entitySize: String,
    val sector: String,
    val idUserCreate: Int
)