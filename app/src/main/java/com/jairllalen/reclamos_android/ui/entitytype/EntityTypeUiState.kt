package com.jairllalen.reclamos_android.ui.entitytype

import com.jairllalen.reclamos_android.data.model.EntityType

data class EntityTypeUiState(

    // =========================
    // LISTADO
    // =========================

    val entities: List<EntityType> = emptyList(),

    val isLoading: Boolean = false,

    val errorMessage: String? = null,

    // =========================
    // FORMULARIO
    // =========================

    val isSaving: Boolean = false,

    val saveErrorMessage: String? = null,

    val saveSuccess: Boolean = false,

    // =========================
    // ELIMINACIÓN
    // =========================

    val isDeleting: Boolean = false,

    val deleteErrorMessage: String? = null,

    val deleteSuccess: Boolean = false,

    // =========================
    // CARGA DE UN REGISTRO
    // =========================

    val isLoadingEntity: Boolean = false,

    val loadingEntityError: String? = null,

    val selectedEntity: EntityType? = null
)