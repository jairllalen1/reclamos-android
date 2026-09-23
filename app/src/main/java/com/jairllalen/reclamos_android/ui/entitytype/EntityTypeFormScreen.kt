package com.jairllalen.reclamos_android.ui.entitytype

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.ExperimentalMaterial3Api

private const val CURRENT_USER_ID = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityTypeFormScreen(
    entityTypeId: Int?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    // =========================================================
    // VIEWMODEL
    // =========================================================

    val viewModel: EntityTypeViewModel = viewModel(
        factory = EntityTypeViewModelFactory()
    )

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    // =========================================================
    // ESTADO DEL MENÚ DE TAMAÑO
    // =========================================================

    var sizeMenuExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    // =========================================================
    // MODO
    // =========================================================

    val isEditing =
        entityTypeId != null

    // =========================================================
    // CARGA INICIAL
    // =========================================================

    LaunchedEffect(entityTypeId) {

        if (entityTypeId == null) {

            viewModel.startCreate()

        } else {

            viewModel.loadEntityTypeById(
                entityTypeId
            )
        }
    }

    // =========================================================
    // VOLVER DESPUÉS DE GUARDAR
    // =========================================================

    LaunchedEffect(uiState.successMessage) {

        if (
            !uiState.successMessage
                .isNullOrBlank()
        ) {

            onBack()
        }
    }

    // =========================================================
    // SCAFFOLD
    // =========================================================

    Scaffold(

        modifier =
            modifier.fillMaxSize(),

        topBar = {

            TopAppBar(

                title = {

                    Text(

                        text =
                            if (isEditing) {
                                "Editar tipo de entidad"
                            } else {
                                "Nuevo tipo de entidad"
                            }
                    )
                },

                navigationIcon = {

                    TextButton(

                        onClick = onBack,

                        enabled =
                            !uiState.isSaving
                    ) {

                        Text(
                            text = "Volver"
                        )
                    }
                }
            )
        }

    ) { innerPadding ->

        // =====================================================
        // CARGANDO REGISTRO PARA EDITAR
        // =====================================================

        if (
            isEditing &&
            uiState.isLoading
        ) {

            Box(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),

                contentAlignment =
                    Alignment.Center
            ) {

                Column(

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Text(
                        text =
                            "Cargando tipo de entidad..."
                    )
                }
            }

        } else {

            // =================================================
            // CONTENIDO
            // =================================================

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {

                // =============================================
                // TÍTULO
                // =============================================

                Text(

                    text =
                        if (isEditing) {
                            "Modificar información"
                        } else {
                            "Registrar nuevo tipo de entidad"
                        },

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )

                Text(

                    text =
                        if (isEditing) {
                            "Actualizá los datos del tipo de entidad."
                        } else {
                            "Completá los datos para registrar un nuevo tipo."
                        }
                )

                // =============================================
                // ERROR GENERAL DE CARGA
                // =============================================

                if (
                    uiState.errorMessage != null &&
                    uiState.editingEntityType == null &&
                    isEditing &&
                    !uiState.isSaving
                ) {

                    Text(

                        text =
                            uiState.errorMessage
                                .orEmpty(),

                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )

                    Button(

                        onClick = {

                            if (entityTypeId != null) {

                                viewModel.loadEntityTypeById(
                                    entityTypeId
                                )
                            }
                        }

                    ) {

                        Text(
                            text = "Reintentar"
                        )
                    }
                }

                // =============================================
                // NOMBRE
                // =============================================

                OutlinedTextField(

                    value =
                        uiState.nameTypeEntity,

                    onValueChange = {
                        viewModel.updateNameTypeEntity(
                            it
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            text =
                                "Nombre del tipo de entidad"
                        )
                    },

                    placeholder = {
                        Text(
                            text =
                                "Ej. Comercio"
                        )
                    },

                    singleLine = true,

                    enabled =
                        !uiState.isSaving,

                    isError =
                        uiState.nameError != null
                )

                if (
                    uiState.nameError != null
                ) {

                    Text(

                        text =
                            uiState.nameError
                                .orEmpty(),

                        color =
                            MaterialTheme
                                .colorScheme
                                .error,

                        style =
                            MaterialTheme
                                .typography
                                .bodySmall
                    )
                }

                // =============================================
                // TAMAÑO
                // =============================================

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(

                        value =
                            uiState.entitySize,

                        onValueChange = {},

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text(
                                text = "Tamaño"
                            )
                        },

                        placeholder = {
                            Text(
                                text =
                                    "Seleccioná un tamaño"
                            )
                        },

                        readOnly = true,

                        enabled =
                            !uiState.isSaving,

                        isError =
                            uiState.sizeError != null,

                        trailingIcon = {

                            Text(
                                text =
                                    if (sizeMenuExpanded) {
                                        "▲"
                                    } else {
                                        "▼"
                                    }
                            )
                        }
                    )

                    TextButton(

                        onClick = {
                            sizeMenuExpanded =
                                true
                        },

                        enabled =
                            !uiState.isSaving,

                        modifier =
                            Modifier.fillMaxSize()
                    ) {

                        Text(
                            text = ""
                        )
                    }

                    DropdownMenu(

                        expanded =
                            sizeMenuExpanded,

                        onDismissRequest = {
                            sizeMenuExpanded =
                                false
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        DropdownMenuItem(

                            text = {
                                Text(
                                    text =
                                        "Pequeña"
                                )
                            },

                            onClick = {

                                viewModel.updateEntitySize(
                                    "Pequeña"
                                )

                                sizeMenuExpanded =
                                    false
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text(
                                    text =
                                        "Mediana"
                                )
                            },

                            onClick = {

                                viewModel.updateEntitySize(
                                    "Mediana"
                                )

                                sizeMenuExpanded =
                                    false
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text(
                                    text =
                                        "Grande"
                                )
                            },

                            onClick = {

                                viewModel.updateEntitySize(
                                    "Grande"
                                )

                                sizeMenuExpanded =
                                    false
                            }
                        )
                    }
                }

                if (
                    uiState.sizeError != null
                ) {

                    Text(

                        text =
                            uiState.sizeError
                                .orEmpty(),

                        color =
                            MaterialTheme
                                .colorScheme
                                .error,

                        style =
                            MaterialTheme
                                .typography
                                .bodySmall
                    )
                }

                // =============================================
                // SECTOR
                // =============================================

                OutlinedTextField(

                    value =
                        uiState.sector,

                    onValueChange = {
                        viewModel.updateSector(
                            it
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            text = "Sector"
                        )
                    },

                    placeholder = {
                        Text(
                            text =
                                "Ej. Comercio"
                        )
                    },

                    singleLine = true,

                    enabled =
                        !uiState.isSaving,

                    isError =
                        uiState.sectorError != null
                )

                if (
                    uiState.sectorError != null
                ) {

                    Text(

                        text =
                            uiState.sectorError
                                .orEmpty(),

                        color =
                            MaterialTheme
                                .colorScheme
                                .error,

                        style =
                            MaterialTheme
                                .typography
                                .bodySmall
                    )
                }

                // =============================================
                // ERROR GENERAL DE API
                // =============================================

                if (
                    !uiState.errorMessage
                        .isNullOrBlank() &&
                    !(
                            isEditing &&
                                    uiState.editingEntityType == null &&
                                    !uiState.isSaving
                            )
                ) {

                    Text(

                        text =
                            uiState.errorMessage
                                .orEmpty(),

                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }

                // =============================================
                // BOTONES
                // =============================================

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    TextButton(

                        onClick =
                            onBack,

                        enabled =
                            !uiState.isSaving,

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Cancelar"
                        )
                    }

                    Button(

                        onClick = {

                            if (isEditing) {

                                viewModel.updateEntityType(
                                    idUserUpdate =
                                        CURRENT_USER_ID
                                )

                            } else {

                                viewModel.createEntityType(
                                    idUserCreate =
                                        CURRENT_USER_ID
                                )
                            }
                        },

                        enabled =
                            !uiState.isSaving &&
                                    !uiState.isLoading,

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        if (uiState.isSaving) {

                            Row(

                                verticalAlignment =
                                    Alignment.CenterVertically,

                                horizontalArrangement =
                                    Arrangement.Center
                            ) {

                                CircularProgressIndicator(

                                    modifier =
                                        Modifier
                                            .height(20.dp),

                                    strokeWidth =
                                        2.dp
                                )

                                Spacer(
                                    modifier =
                                        Modifier.padding(
                                            horizontal = 4.dp
                                        )
                                )

                                Text(

                                    text =
                                        if (isEditing) {
                                            "Actualizando..."
                                        } else {
                                            "Creando..."
                                        }
                                )
                            }

                        } else {

                            Text(

                                text =
                                    if (isEditing) {
                                        "Actualizar"
                                    } else {
                                        "Crear"
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}