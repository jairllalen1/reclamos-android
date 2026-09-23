package com.jairllalen.reclamos_android.ui.entitytype

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jairllalen.reclamos_android.data.model.EntityType
import com.jairllalen.reclamos_android.data.model.EntityTypeCreateRequest
import com.jairllalen.reclamos_android.data.model.EntityTypeUpdateRequest
import com.jairllalen.reclamos_android.data.remote.NetworkResult
import com.jairllalen.reclamos_android.data.repository.EntityTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EntityTypeViewModel(
    private val repository: EntityTypeRepository = EntityTypeRepository()
) : ViewModel() {

    // =========================================================
    // ESTADO
    // =========================================================

    private val _uiState = MutableStateFlow(
        EntityTypeUiState()
    )

    val uiState: StateFlow<EntityTypeUiState> =
        _uiState.asStateFlow()


    // =========================================================
    // LISTAR
    // =========================================================

    fun loadEntityTypes() {

        // Evita iniciar otra carga mientras una ya está en curso.
        if (_uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = repository.getAllEntityTypes()
            ) {

                is NetworkResult.Success -> {

                    _uiState.value = _uiState.value.copy(
                        entityTypes = result.data,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is NetworkResult.HttpError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = buildHttpErrorMessage(
                            result.code,
                            result.message
                        )
                    )
                }

                is NetworkResult.NetworkError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "No se pudo conectar con el servidor."
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "Ocurrió un error inesperado."
                    )
                }
            }
        }
    }


    // =========================================================
    // OBTENER UNO
    // =========================================================

    fun loadEntityTypeById(id: Int) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = repository.getEntityTypeById(id)
            ) {

                is NetworkResult.Success -> {

                    val entityType = result.data

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,

                        editingEntityType = entityType,

                        nameTypeEntity =
                            entityType.nameTypeEntity,

                        entitySize =
                            entityType.entitySize,

                        sector =
                            entityType.sector,

                        nameError = null,
                        sizeError = null,
                        sectorError = null,

                        errorMessage = null
                    )
                }

                is NetworkResult.HttpError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = buildHttpErrorMessage(
                            result.code,
                            result.message
                        )
                    )
                }

                is NetworkResult.NetworkError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "No se pudo conectar con el servidor."
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "Ocurrió un error inesperado."
                    )
                }
            }
        }
    }


    // =========================================================
    // FORMULARIO - CREAR
    // =========================================================

    fun startCreate() {

        _uiState.value = _uiState.value.copy(

            editingEntityType = null,

            nameTypeEntity = "",

            entitySize = "",

            sector = "",

            nameError = null,

            sizeError = null,

            sectorError = null,

            errorMessage = null,

            successMessage = null,

            isSaving = false,

            isDeleting = false
        )
    }


    // =========================================================
    // FORMULARIO - EDITAR
    // =========================================================

    fun startEdit(entityType: EntityType) {

        _uiState.value = _uiState.value.copy(

            editingEntityType = entityType,

            nameTypeEntity =
                entityType.nameTypeEntity,

            entitySize =
                entityType.entitySize,

            sector =
                entityType.sector,

            nameError = null,

            sizeError = null,

            sectorError = null,

            errorMessage = null,

            successMessage = null
        )
    }


    // =========================================================
    // CAMPO NOMBRE
    // =========================================================

    fun updateNameTypeEntity(value: String) {

        _uiState.value = _uiState.value.copy(

            nameTypeEntity = value,

            nameError = null,

            errorMessage = null
        )
    }


    // =========================================================
    // CAMPO TAMAÑO
    // =========================================================

    fun updateEntitySize(value: String) {

        _uiState.value = _uiState.value.copy(

            entitySize = value,

            sizeError = null,

            errorMessage = null
        )
    }


    // =========================================================
    // CAMPO SECTOR
    // =========================================================

    fun updateSector(value: String) {

        _uiState.value = _uiState.value.copy(

            sector = value,

            sectorError = null,

            errorMessage = null
        )
    }


    // =========================================================
    // CREAR
    // =========================================================

    fun createEntityType(idUserCreate: Int) {

        // -----------------------------------------------------
        // BLOQUEO INMEDIATO CONTRA DOBLE TOQUE
        // -----------------------------------------------------

        if (_uiState.value.isSaving) {
            return
        }

        val currentState = _uiState.value

        val name =
            currentState.nameTypeEntity.trim()

        val size =
            currentState.entitySize.trim()

        val sector =
            currentState.sector.trim()

        // -----------------------------------------------------
        // VALIDACIÓN
        // -----------------------------------------------------

        if (
            !validateForm(
                name = name,
                size = size,
                sector = sector
            )
        ) {
            return
        }

        // -----------------------------------------------------
        // BLOQUEAMOS ANTES DE LA COROUTINE
        // -----------------------------------------------------

        _uiState.value = _uiState.value.copy(

            isSaving = true,

            errorMessage = null,

            successMessage = null
        )

        // -----------------------------------------------------
        // PETICIÓN
        // -----------------------------------------------------

        viewModelScope.launch {

            val request = EntityTypeCreateRequest(

                nameTypeEntity = name,

                entitySize = size,

                sector = sector,

                idUserCreate = idUserCreate
            )

            when (
                val result =
                    repository.createEntityType(request)
            ) {

                is NetworkResult.Success -> {

                    _uiState.value = _uiState.value.copy(

                        isSaving = false,

                        successMessage =
                            "Tipo de entidad creado correctamente.",

                        errorMessage = null
                    )
                }

                is NetworkResult.HttpError -> {

                    _uiState.value = _uiState.value.copy(

                        isSaving = false,

                        errorMessage =
                            buildHttpErrorMessage(
                                result.code,
                                result.message
                            ),

                        successMessage = null
                    )
                }

                is NetworkResult.NetworkError -> {

                    _uiState.value = _uiState.value.copy(

                        isSaving = false,

                        errorMessage =
                            "No se pudo conectar con el servidor.",

                        successMessage = null
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(

                        isSaving = false,

                        errorMessage =
                            "Ocurrió un error inesperado.",

                        successMessage = null
                    )
                }
            }
        }
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

    fun updateEntityType(idUserUpdate: Int) {

        // -----------------------------------------------------
        // BLOQUEO INMEDIATO CONTRA DOBLE TOQUE
        // -----------------------------------------------------

        if (_uiState.value.isSaving) {
            return
        }

        val currentState = _uiState.value

        val entityType =
            currentState.editingEntityType

        if (entityType == null) {

            _uiState.value = _uiState.value.copy(

                errorMessage =
                    "No se encontró el tipo de entidad que se desea editar."
            )

            return
        }

        val name =
            currentState.nameTypeEntity.trim()

        val size =
            currentState.entitySize.trim()

        val sector =
            currentState.sector.trim()

        // -----------------------------------------------------
        // VALIDACIÓN
        // -----------------------------------------------------

        if (
            !validateForm(
                name = name,
                size = size,
                sector = sector
            )
        ) {
            return
        }

        // -----------------------------------------------------
        // BLOQUEAMOS ANTES DE LA COROUTINE
        // -----------------------------------------------------

        _uiState.value = _uiState.value.copy(

            isSaving = true,

            errorMessage = null,

            successMessage = null
        )

        // -----------------------------------------------------
        // PETICIÓN
        // -----------------------------------------------------

        viewModelScope.launch {

            val request = EntityTypeUpdateRequest(

                nameTypeEntity = name,

                entitySize = size,

                sector = sector,

                idUserUpdate = idUserUpdate
            )

            when (
                val result =
                    repository.updateEntityType(
                        id = entityType.idEntityType,
                        request = request
                    )
            ) {

                is NetworkResult.Success -> {

                    _uiState.value = _uiState.value.copy(

                        isSaving = false,

                        successMessage =
                            "Tipo de entidad actualizado correctamente.",

                        errorMessage = null
                    )
                }

                is NetworkResult.HttpError -> {

                    _uiState.value = _uiState.value.copy(

                        isSaving = false,

                        errorMessage =
                            buildHttpErrorMessage(
                                result.code,
                                result.message
                            ),

                        successMessage = null
                    )
                }

                is NetworkResult.NetworkError -> {

                    _uiState.value = _uiState.value.copy(

                        isSaving = false,

                        errorMessage =
                            "No se pudo conectar con el servidor.",

                        successMessage = null
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(

                        isSaving = false,

                        errorMessage =
                            "Ocurrió un error inesperado.",

                        successMessage = null
                    )
                }
            }
        }
    }


    // =========================================================
    // SOLICITAR ELIMINACIÓN
    // =========================================================

    fun requestDelete(entityType: EntityType) {

        _uiState.value = _uiState.value.copy(

            entityTypeToDelete = entityType,

            errorMessage = null
        )
    }


    // =========================================================
    // CANCELAR ELIMINACIÓN
    // =========================================================

    fun cancelDelete() {

        if (_uiState.value.isDeleting) {
            return
        }

        _uiState.value = _uiState.value.copy(

            entityTypeToDelete = null
        )
    }


    // =========================================================
    // ELIMINAR
    // =========================================================

    fun deleteEntityType() {

        // -----------------------------------------------------
        // BLOQUEO INMEDIATO CONTRA DOBLE TOQUE
        // -----------------------------------------------------

        if (_uiState.value.isDeleting) {
            return
        }

        val currentState = _uiState.value

        val entityType =
            currentState.entityTypeToDelete

        if (entityType == null) {
            return
        }

        // -----------------------------------------------------
        // BLOQUEAMOS ANTES DE LA COROUTINE
        // -----------------------------------------------------

        _uiState.value = _uiState.value.copy(

            isDeleting = true,

            errorMessage = null,

            successMessage = null
        )

        // -----------------------------------------------------
        // PETICIÓN
        // -----------------------------------------------------

        viewModelScope.launch {

            when (
                val result =
                    repository.deleteEntityType(
                        entityType.idEntityType
                    )
            ) {

                is NetworkResult.Success -> {

                    val updatedList =
                        _uiState.value.entityTypes.filter {
                            it.idEntityType !=
                                    entityType.idEntityType
                        }

                    _uiState.value = _uiState.value.copy(

                        entityTypes = updatedList,

                        isDeleting = false,

                        entityTypeToDelete = null,

                        successMessage =
                            "Tipo de entidad eliminado correctamente.",

                        errorMessage = null
                    )
                }

                is NetworkResult.HttpError -> {

                    _uiState.value = _uiState.value.copy(

                        isDeleting = false,

                        errorMessage =
                            buildHttpErrorMessage(
                                result.code,
                                result.message
                            ),

                        successMessage = null
                    )
                }

                is NetworkResult.NetworkError -> {

                    _uiState.value = _uiState.value.copy(

                        isDeleting = false,

                        errorMessage =
                            "No se pudo conectar con el servidor.",

                        successMessage = null
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(

                        isDeleting = false,

                        errorMessage =
                            "Ocurrió un error inesperado.",

                        successMessage = null
                    )
                }
            }
        }
    }


    // =========================================================
    // VALIDACIÓN
    // =========================================================

    private fun validateForm(
        name: String,
        size: String,
        sector: String
    ): Boolean {

        var isValid = true

        var nameError: String? = null

        var sizeError: String? = null

        var sectorError: String? = null


        // -----------------------------------------------------
        // NOMBRE
        // -----------------------------------------------------

        if (name.isBlank()) {

            nameError =
                "El nombre del tipo de entidad es obligatorio."

            isValid = false

        } else if (name.length > 45) {

            nameError =
                "El nombre no puede superar los 45 caracteres."

            isValid = false
        }


        // -----------------------------------------------------
        // TAMAÑO
        // -----------------------------------------------------

        if (size.isBlank()) {

            sizeError =
                "El tamaño de la entidad es obligatorio."

            isValid = false

        } else if (size.length > 45) {

            sizeError =
                "El tamaño no puede superar los 45 caracteres."

            isValid = false
        }


        // -----------------------------------------------------
        // SECTOR
        // -----------------------------------------------------

        if (sector.isBlank()) {

            sectorError =
                "El sector es obligatorio."

            isValid = false

        } else if (sector.length > 45) {

            sectorError =
                "El sector no puede superar los 45 caracteres."

            isValid = false
        }


        _uiState.value = _uiState.value.copy(

            nameError = nameError,

            sizeError = sizeError,

            sectorError = sectorError
        )

        return isValid
    }


    // =========================================================
    // LIMPIAR MENSAJES
    // =========================================================

    fun clearMessages() {

        _uiState.value = _uiState.value.copy(

            errorMessage = null,

            successMessage = null
        )
    }


    // =========================================================
    // MENSAJES HTTP
    // =========================================================

    private fun buildHttpErrorMessage(
        code: Int,
        message: String?
    ): String {

        return when (code) {

            400 ->
                message
                    ?: "La información enviada no es válida."

            401 ->
                "No estás autenticado."

            403 ->
                "No tenés permiso para realizar esta operación."

            404 ->
                message
                    ?: "No se encontró el tipo de entidad."

            409 ->
                message
                    ?: "La operación entra en conflicto con los datos existentes."

            500 ->
                "El servidor encontró un error interno."

            503 ->
                "El servidor no está disponible."

            else ->
                message
                    ?: "La solicitud falló. Código HTTP: $code"
        }
    }
}