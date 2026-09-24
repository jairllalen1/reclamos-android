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
import androidx.compose.material3.AlertDialog
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
    // ESTADOS LOCALES DE CONFIRMACIÓN
    // =========================================================

    var sizeMenuExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var showCreateConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    var showUpdateConfirmation by rememberSaveable {
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
    // PANTALLA
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
        // CARGANDO EDICIÓN
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
            // FORMULARIO
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
                // ERROR DE CARGA EN EDICIÓN
                // =============================================

                if (
                    isEditing &&
                    uiState.editingEntityType == null &&
                    !uiState.errorMessage.isNullOrBlank() &&
                    !uiState.isLoading
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

                            if (
                                entityTypeId != null
                            ) {

                                viewModel
                                    .loadEntityTypeById(
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
                        viewModel
                            .updateNameTypeEntity(
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
                                    if (
                                        sizeMenuExpanded
                                    ) {
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

                                viewModel
                                    .updateEntitySize(
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

                                viewModel
                                    .updateEntitySize(
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

                                viewModel
                                    .updateEntitySize(
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
                        viewModel
                            .updateSector(it)
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
                // ERROR GENERAL
                // =============================================

                if (
                    !uiState.errorMessage
                        .isNullOrBlank() &&
                    !(
                            isEditing &&
                                    uiState.editingEntityType == null
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

                        onClick = onBack,

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

                            if (uiState.isSaving) {
                                return@Button
                            }

                            // Primero abrimos confirmación.
                            if (isEditing) {

                                showUpdateConfirmation =
                                    true

                            } else {

                                showCreateConfirmation =
                                    true
                            }
                        },

                        enabled =
                            !uiState.isSaving &&
                                    !uiState.isLoading,

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        if (uiState.isSaving) {

                            CircularProgressIndicator(

                                modifier =
                                    Modifier.height(20.dp),

                                strokeWidth = 2.dp
                            )

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

    // =========================================================
    // DIÁLOGO CONFIRMAR CREACIÓN
    // =========================================================

    if (showCreateConfirmation) {

        AlertDialog(

            onDismissRequest = {

                if (!uiState.isSaving) {

                    showCreateConfirmation =
                        false
                }
            },

            title = {

                Text(
                    text =
                        "Confirmar creación"
                )
            },

            text = {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text =
                            "¿Querés crear este tipo de entidad?"
                    )

                    Text(
                        text =
                            "Nombre: ${uiState.nameTypeEntity}"
                    )

                    Text(
                        text =
                            "Tamaño: ${uiState.entitySize}"
                    )

                    Text(
                        text =
                            "Sector: ${uiState.sector}"
                    )
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        if (uiState.isSaving) {
                            return@Button
                        }

                        showCreateConfirmation =
                            false

                        viewModel.createEntityType(
                            idUserCreate =
                                CURRENT_USER_ID
                        )
                    },

                    enabled =
                        !uiState.isSaving
                ) {

                    Text(
                        text = "Crear"
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        if (!uiState.isSaving) {

                            showCreateConfirmation =
                                false
                        }
                    },

                    enabled =
                        !uiState.isSaving
                ) {

                    Text(
                        text = "Cancelar"
                    )
                }
            }
        )
    }

    // =========================================================
    // DIÁLOGO CONFIRMAR ACTUALIZACIÓN
    // =========================================================

    if (showUpdateConfirmation) {

        AlertDialog(

            onDismissRequest = {

                if (!uiState.isSaving) {

                    showUpdateConfirmation =
                        false
                }
            },

            title = {

                Text(
                    text =
                        "Confirmar cambios"
                )
            },

            text = {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text =
                            "¿Querés actualizar este tipo de entidad?"
                    )

                    Text(
                        text =
                            "Nombre: ${uiState.nameTypeEntity}"
                    )

                    Text(
                        text =
                            "Tamaño: ${uiState.entitySize}"
                    )

                    Text(
                        text =
                            "Sector: ${uiState.sector}"
                    )
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        if (uiState.isSaving) {
                            return@Button
                        }

                        showUpdateConfirmation =
                            false

                        viewModel.updateEntityType(
                            idUserUpdate =
                                CURRENT_USER_ID
                        )
                    },

                    enabled =
                        !uiState.isSaving
                ) {

                    Text(
                        text = "Actualizar"
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        if (!uiState.isSaving) {

                            showUpdateConfirmation =
                                false
                        }
                    },

                    enabled =
                        !uiState.isSaving
                ) {

                    Text(
                        text = "Cancelar"
                    )
                }
            }
        )
    }
}