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
import androidx.compose.material3.ExperimentalMaterial3Api
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

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // =========================================================
    // ESTADOS LOCALES DEL FORMULARIO
    // =========================================================

    var nameTypeEntity by rememberSaveable {
        mutableStateOf("")
    }

    var entitySize by rememberSaveable {
        mutableStateOf("")
    }

    var sector by rememberSaveable {
        mutableStateOf("")
    }

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

    val isEditing = entityTypeId != null

    // =========================================================
    // CARGA INICIAL
    // =========================================================

    LaunchedEffect(entityTypeId) {

        if (entityTypeId == null) {

            nameTypeEntity = ""
            entitySize = ""
            sector = ""

        } else {

            viewModel.loadEntity(entityTypeId)
        }
    }

    // =========================================================
    // CARGAR DATOS RECIBIDOS DEL SERVIDOR EN EL FORMULARIO
    // =========================================================

    LaunchedEffect(uiState.selectedEntity) {

        uiState.selectedEntity?.let { entity ->

            nameTypeEntity = entity.nameTypeEntity
            entitySize = entity.entitySize
            sector = entity.sector
        }
    }

    // =========================================================
    // VOLVER DESPUÉS DE GUARDAR
    // =========================================================

    LaunchedEffect(uiState.saveSuccess) {

        if (uiState.saveSuccess) {
            onBack()
        }
    }

    // =========================================================
    // PANTALLA
    // =========================================================

    Scaffold(

        modifier = modifier.fillMaxSize(),

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
                        enabled = !uiState.isSaving
                    ) {

                        Text("Volver")
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
            uiState.isLoadingEntity
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = "Cargando tipo de entidad..."
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
                        MaterialTheme.typography.titleMedium
                )

                Text(
                    text =
                        if (isEditing) {
                            "Actualizá los datos del tipo de entidad."
                        } else {
                            "Completá los datos para registrar un nuevo tipo."
                        }
                )

                // =================================================
                // ERROR AL CARGAR EDICIÓN
                // =================================================

                if (
                    isEditing &&
                    uiState.loadingEntityError != null
                ) {

                    Text(
                        text = uiState.loadingEntityError.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(
                        onClick = {

                            if (entityTypeId != null) {
                                viewModel.loadEntity(entityTypeId)
                            }
                        }
                    ) {

                        Text("Reintentar")
                    }
                }

                // =================================================
                // NOMBRE
                // =================================================

                OutlinedTextField(

                    value = nameTypeEntity,

                    onValueChange = {
                        nameTypeEntity = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Nombre del tipo de entidad")
                    },

                    placeholder = {
                        Text("Ej. Comercio")
                    },

                    singleLine = true,

                    enabled =
                        !uiState.isSaving &&
                                !uiState.isLoadingEntity
                )

                // =================================================
                // TAMAÑO
                // =================================================

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(

                        value = entitySize,

                        onValueChange = {},

                        modifier = Modifier.fillMaxWidth(),

                        label = {
                            Text("Tamaño")
                        },

                        placeholder = {
                            Text("Seleccioná un tamaño")
                        },

                        readOnly = true,

                        enabled =
                            !uiState.isSaving &&
                                    !uiState.isLoadingEntity,

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
                            sizeMenuExpanded = true
                        },

                        enabled =
                            !uiState.isSaving &&
                                    !uiState.isLoadingEntity,

                        modifier = Modifier.fillMaxSize()

                    ) {

                        Text("")
                    }

                    DropdownMenu(

                        expanded = sizeMenuExpanded,

                        onDismissRequest = {
                            sizeMenuExpanded = false
                        },

                        modifier = Modifier.fillMaxWidth()

                    ) {

                        DropdownMenuItem(

                            text = {
                                Text("Pequeña")
                            },

                            onClick = {

                                entitySize = "Pequeña"
                                sizeMenuExpanded = false
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text("Mediana")
                            },

                            onClick = {

                                entitySize = "Mediana"
                                sizeMenuExpanded = false
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text("Grande")
                            },

                            onClick = {

                                entitySize = "Grande"
                                sizeMenuExpanded = false
                            }
                        )
                    }
                }

                // =================================================
                // SECTOR
                // =================================================

                OutlinedTextField(

                    value = sector,

                    onValueChange = {
                        sector = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Sector")
                    },

                    placeholder = {
                        Text("Ej. Comercio")
                    },

                    singleLine = true,

                    enabled =
                        !uiState.isSaving &&
                                !uiState.isLoadingEntity
                )

                // =================================================
                // ERROR DE GUARDADO
                // =================================================

                if (uiState.saveErrorMessage != null) {

                    Text(
                        text = uiState.saveErrorMessage.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // =================================================
                // BOTONES
                // =================================================

                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)

                ) {

                    TextButton(

                        onClick = onBack,

                        enabled = !uiState.isSaving,

                        modifier = Modifier.weight(1f)

                    ) {

                        Text("Cancelar")
                    }

                    Button(

                        onClick = {

                            if (uiState.isSaving) {
                                return@Button
                            }

                            if (isEditing) {

                                showUpdateConfirmation = true

                            } else {

                                showCreateConfirmation = true
                            }
                        },

                        enabled =
                            !uiState.isSaving &&
                                    !uiState.isLoadingEntity,

                        modifier = Modifier.weight(1f)

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
    // CONFIRMAR CREACIÓN
    // =========================================================

    if (showCreateConfirmation) {

        AlertDialog(

            onDismissRequest = {

                if (!uiState.isSaving) {
                    showCreateConfirmation = false
                }
            },

            title = {
                Text("Confirmar creación")
            },

            text = {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        "¿Querés crear este tipo de entidad?"
                    )

                    Text(
                        "Nombre: $nameTypeEntity"
                    )

                    Text(
                        "Tamaño: $entitySize"
                    )

                    Text(
                        "Sector: $sector"
                    )
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        if (uiState.isSaving) {
                            return@Button
                        }

                        showCreateConfirmation = false

                        viewModel.createEntity(
                            name = nameTypeEntity,
                            size = entitySize,
                            sector = sector
                        )
                    },

                    enabled = !uiState.isSaving

                ) {

                    Text("Crear")
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        if (!uiState.isSaving) {
                            showCreateConfirmation = false
                        }
                    },

                    enabled = !uiState.isSaving

                ) {

                    Text("Cancelar")
                }
            }
        )
    }

    // =========================================================
    // CONFIRMAR ACTUALIZACIÓN
    // =========================================================

    if (showUpdateConfirmation) {

        AlertDialog(

            onDismissRequest = {

                if (!uiState.isSaving) {
                    showUpdateConfirmation = false
                }
            },

            title = {
                Text("Confirmar cambios")
            },

            text = {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        "¿Querés actualizar este tipo de entidad?"
                    )

                    Text(
                        "Nombre: $nameTypeEntity"
                    )

                    Text(
                        "Tamaño: $entitySize"
                    )

                    Text(
                        "Sector: $sector"
                    )
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        if (uiState.isSaving) {
                            return@Button
                        }

                        if (entityTypeId == null) {
                            return@Button
                        }

                        showUpdateConfirmation = false

                        viewModel.updateEntity(
                            id = entityTypeId,
                            name = nameTypeEntity,
                            size = entitySize,
                            sector = sector
                        )
                    },

                    enabled = !uiState.isSaving

                ) {

                    Text("Actualizar")
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        if (!uiState.isSaving) {
                            showUpdateConfirmation = false
                        }
                    },

                    enabled = !uiState.isSaving

                ) {

                    Text("Cancelar")
                }
            }
        )
    }
}