package com.jairllalen.reclamos_android.ui.entitytype

import com.jairllalen.reclamos_android.data.model.EntityType

data class EntityTypeUiState(

    // =========================================================
    // LISTADO
    // =========================================================

    val entityTypes: List<EntityType> = emptyList(),

    val isLoading: Boolean = false,

    val errorMessage: String? = null,

    // =========================================================
    // FORMULARIO
    // =========================================================

    val isSaving: Boolean = false,

    val editingEntityType: EntityType? = null,

    val nameTypeEntity: String = "",

    val entitySize: String = "",

    val sector: String = "",

    // Errores específicos de cada campo
    val nameError: String? = null,

    val sizeError: String? = null,

    val sectorError: String? = null,

    // =========================================================
    // ELIMINACIÓN
    // =========================================================

    val isDeleting: Boolean = false,

    val entityTypeToDelete: EntityType? = null,

    // =========================================================
    // MENSAJES
    // =========================================================

    val successMessage: String? = null
)