package com.jairllalen.reclamos_android.data.model

data class EntityType(
    val idEntityType: Int,
    val nameTypeEntity: String,
    val entitySize: String,
    val sector: String,
    val idUserCreate: Int,
    val idUserUpdate: Int?,
    val dateCreate: String?,
    val dateUpdate: String?
)