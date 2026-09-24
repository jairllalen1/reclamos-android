package com.jairllalen.reclamos_android.ui.entitytype

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jairllalen.reclamos_android.data.model.EntityTypeCreateRequest
import com.jairllalen.reclamos_android.data.model.EntityTypeUpdateRequest
import com.jairllalen.reclamos_android.data.remote.NetworkResult
import com.jairllalen.reclamos_android.data.repository.EntityTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EntityTypeViewModel(
    private val repository: EntityTypeRepository
) : ViewModel() {

    companion object {
        private const val CURRENT_USER_ID = 1
        private const val MAX_FIELD_LENGTH = 45
    }

    private val _uiState = MutableStateFlow(EntityTypeUiState())
    val uiState: StateFlow<EntityTypeUiState> = _uiState.asStateFlow()

    // ============================================================
    // LISTAR
    // ============================================================

    fun loadEntities() {

        if (_uiState.value.isLoading) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {

            when (val result = repository.getAllEntityTypes()) {

                is NetworkResult.Success<*> -> {

                    @Suppress("UNCHECKED_CAST")
                    val entities =
                        result.data as List<com.jairllalen.reclamos_android.data.model.EntityType>

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        entities = entities,
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
                        errorMessage = "No se puede conectar con el servidor."
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Ocurrió un error inesperado."
                    )
                }
            }
        }
    }

    // ============================================================
    // OBTENER POR ID
    // ============================================================

    fun loadEntity(id: Int) {

        if (_uiState.value.isLoadingEntity) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoadingEntity = true,
            loadingEntityError = null,
            selectedEntity = null
        )

        viewModelScope.launch {

            when (val result = repository.getEntityTypeById(id)) {

                is NetworkResult.Success<*> -> {

                    @Suppress("UNCHECKED_CAST")
                    val entity =
                        result.data as com.jairllalen.reclamos_android.data.model.EntityType

                    _uiState.value = _uiState.value.copy(
                        isLoadingEntity = false,
                        selectedEntity = entity,
                        loadingEntityError = null
                    )
                }

                is NetworkResult.HttpError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoadingEntity = false,
                        loadingEntityError = buildHttpErrorMessage(
                            result.code,
                            result.message
                        )
                    )
                }

                is NetworkResult.NetworkError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoadingEntity = false,
                        loadingEntityError =
                            "No se puede conectar con el servidor."
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(
                        isLoadingEntity = false,
                        loadingEntityError =
                            "Ocurrió un error inesperado."
                    )
                }
            }
        }
    }

    // ============================================================
    // CREAR
    // ============================================================

    fun createEntity(
        name: String,
        size: String,
        sector: String
    ) {

        if (_uiState.value.isSaving) {
            return
        }

        val validationError = validateFields(
            name = name,
            size = size,
            sector = sector
        )

        if (validationError != null) {

            _uiState.value = _uiState.value.copy(
                saveErrorMessage = validationError
            )

            return
        }

        _uiState.value = _uiState.value.copy(
            isSaving = true,
            saveErrorMessage = null,
            saveSuccess = false
        )

        viewModelScope.launch {

            val request = EntityTypeCreateRequest(
                nameTypeEntity = name.trim(),
                entitySize = size.trim(),
                sector = sector.trim(),
                idUserCreate = CURRENT_USER_ID
            )

            when (val result = repository.createEntityType(request)) {

                is NetworkResult.Success<*> -> {

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = true,
                        saveErrorMessage = null
                    )
                }

                is NetworkResult.HttpError -> {

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = false,
                        saveErrorMessage =
                            buildHttpErrorMessage(
                                result.code,
                                result.message
                            )
                    )
                }

                is NetworkResult.NetworkError -> {

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = false,
                        saveErrorMessage =
                            "No se puede conectar con el servidor."
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = false,
                        saveErrorMessage =
                            "Ocurrió un error inesperado."
                    )
                }
            }
        }
    }

    // ============================================================
    // MODIFICAR
    // ============================================================

    fun updateEntity(
        id: Int,
        name: String,
        size: String,
        sector: String
    ) {

        if (_uiState.value.isSaving) {
            return
        }

        val validationError = validateFields(
            name = name,
            size = size,
            sector = sector
        )

        if (validationError != null) {

            _uiState.value = _uiState.value.copy(
                saveErrorMessage = validationError
            )

            return
        }

        _uiState.value = _uiState.value.copy(
            isSaving = true,
            saveErrorMessage = null,
            saveSuccess = false
        )

        viewModelScope.launch {

            val request = EntityTypeUpdateRequest(
                nameTypeEntity = name.trim(),
                entitySize = size.trim(),
                sector = sector.trim(),
                idUserUpdate = CURRENT_USER_ID
            )

            when (val result = repository.updateEntityType(id, request)) {

                is NetworkResult.Success<*> -> {

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = true,
                        saveErrorMessage = null
                    )
                }

                is NetworkResult.HttpError -> {

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = false,
                        saveErrorMessage =
                            buildHttpErrorMessage(
                                result.code,
                                result.message
                            )
                    )
                }

                is NetworkResult.NetworkError -> {

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = false,
                        saveErrorMessage =
                            "No se puede conectar con el servidor."
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = false,
                        saveErrorMessage =
                            "Ocurrió un error inesperado."
                    )
                }
            }
        }
    }

    // ============================================================
    // ELIMINAR
    // ============================================================

    fun deleteEntity(id: Int) {

        if (_uiState.value.isDeleting) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isDeleting = true,
            deleteErrorMessage = null,
            deleteSuccess = false
        )

        viewModelScope.launch {

            when (val result = repository.deleteEntityType(id)) {

                is NetworkResult.Success<*> -> {

                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        deleteSuccess = true,
                        deleteErrorMessage = null,

                        entities = _uiState.value.entities.filter {
                            it.idEntityType != id
                        }
                    )
                }

                is NetworkResult.HttpError -> {

                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        deleteSuccess = false,
                        deleteErrorMessage =
                            buildHttpErrorMessage(
                                result.code,
                                result.message
                            )
                    )
                }

                is NetworkResult.NetworkError -> {

                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        deleteSuccess = false,
                        deleteErrorMessage =
                            "No se puede conectar con el servidor."
                    )
                }

                is NetworkResult.UnknownError -> {

                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        deleteSuccess = false,
                        deleteErrorMessage =
                            "Ocurrió un error inesperado."
                    )
                }
            }
        }
    }

    // ============================================================
    // LIMPIAR MENSAJES
    // ============================================================

    fun clearSaveMessage() {

        _uiState.value = _uiState.value.copy(
            saveErrorMessage = null,
            saveSuccess = false
        )
    }

    fun clearDeleteMessage() {

        _uiState.value = _uiState.value.copy(
            deleteErrorMessage = null,
            deleteSuccess = false
        )
    }

    fun clearLoadingEntityError() {

        _uiState.value = _uiState.value.copy(
            loadingEntityError = null
        )
    }

    // ============================================================
    // VALIDACIÓN
    // ============================================================

    private fun validateFields(
        name: String,
        size: String,
        sector: String
    ): String? {

        if (name.isBlank()) {
            return "El nombre de la entidad es obligatorio."
        }

        if (size.isBlank()) {
            return "El tamaño de la entidad es obligatorio."
        }

        if (sector.isBlank()) {
            return "El sector de la entidad es obligatorio."
        }

        if (name.length > MAX_FIELD_LENGTH) {
            return "El nombre no puede superar los $MAX_FIELD_LENGTH caracteres."
        }

        if (size.length > MAX_FIELD_LENGTH) {
            return "El tamaño no puede superar los $MAX_FIELD_LENGTH caracteres."
        }

        if (sector.length > MAX_FIELD_LENGTH) {
            return "El sector no puede superar los $MAX_FIELD_LENGTH caracteres."
        }

        return null
    }

    // ============================================================
    // MENSAJES HTTP
    // ============================================================

    private fun buildHttpErrorMessage(
        code: Int,
        serverMessage: String
    ): String {

        return when (code) {

            400 ->
                "Los datos enviados no son válidos."

            401 ->
                "No autorizado. Verificá tus credenciales."

            403 ->
                "No tenés permisos para realizar esta operación."

            404 ->
                "El recurso solicitado no existe."

            409 ->
                "No se puede realizar la operación porque existe un conflicto."

            500 ->
                "El servidor encontró un error interno."

            else ->
                "Error del servidor (HTTP $code)."
        }
    }
}