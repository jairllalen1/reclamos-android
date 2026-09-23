package com.jairllalen.reclamos_android.ui.entitytype

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityTypeListScreen(
    onCreate: () -> Unit,
    onEdit: (Int) -> Unit,
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
    // SNACKBAR
    // =========================================================

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }

    // =========================================================
    // BÚSQUEDA
    // =========================================================

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    // =========================================================
    // CICLO DE VIDA
    // =========================================================

    /*
     * Se ejecuta al entrar en primer plano.
     *
     * Esto hace que:
     *
     * Lista
     *   ↓
     * Formulario
     *   ↓
     * Volver
     *   ↓
     * ON_RESUME
     *   ↓
     * GET actualizado
     *
     * LifecycleEventEffect es la API de Compose para reaccionar
     * a un evento puntual del ciclo de vida.
     */
    LifecycleEventEffect(
        event = Lifecycle.Event.ON_RESUME
    ) {

        viewModel.loadEntityTypes()
    }

    // =========================================================
    // MENSAJE DE ÉXITO
    // =========================================================

    LaunchedEffect(uiState.successMessage) {

        val message =
            uiState.successMessage

        if (!message.isNullOrBlank()) {

            snackbarHostState.showSnackbar(
                message = message
            )

            viewModel.clearMessages()
        }
    }

    // =========================================================
    // FILTRADO
    // =========================================================

    val filteredEntityTypes =
        remember(
            uiState.entityTypes,
            searchQuery
        ) {

            val query =
                searchQuery.trim()

            if (query.isBlank()) {

                uiState.entityTypes

            } else {

                uiState.entityTypes.filter { entityType ->

                    entityType.nameTypeEntity
                        .contains(
                            query,
                            ignoreCase = true
                        ) ||

                            entityType.entitySize
                                .contains(
                                    query,
                                    ignoreCase = true
                                ) ||

                            entityType.sector
                                .contains(
                                    query,
                                    ignoreCase = true
                                ) ||

                            entityType.idEntityType
                                .toString()
                                .contains(query)
                }
            }
        }

    // =========================================================
    // SCAFFOLD
    // =========================================================

    Scaffold(

        modifier = modifier.fillMaxSize(),

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Tipos de entidad"
                    )
                },

                actions = {

                    TextButton(

                        onClick = {
                            viewModel.loadEntityTypes()
                        },

                        enabled =
                            !uiState.isLoading &&
                                    !uiState.isDeleting
                    ) {

                        Text(
                            text = "Actualizar"
                        )
                    }
                }
            )
        },

        floatingActionButton = {

            FloatingActionButton(

                onClick = {

                    viewModel.startCreate()

                    onCreate()
                },

                containerColor =
                    MaterialTheme
                        .colorScheme
                        .primaryContainer
            ) {

                Text(
                    text = "+"
                )
            }
        },

        snackbarHost = {

            SnackbarHost(
                hostState = snackbarHostState
            )
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // =================================================
            // INDICADOR DE REFRESH
            // =================================================

            if (
                uiState.isLoading &&
                uiState.entityTypes.isNotEmpty()
            ) {

                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // =================================================
            // BUSCADOR
            // =================================================

            OutlinedTextField(

                value = searchQuery,

                onValueChange = {
                    searchQuery = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),

                label = {
                    Text(
                        text =
                            "Buscar tipo de entidad"
                    )
                },

                placeholder = {
                    Text(
                        text =
                            "Nombre, tamaño, sector o ID"
                    )
                },

                singleLine = true,

                enabled =
                    !uiState.isDeleting
            )

            // =================================================
            // ERROR SIN DATOS
            // =================================================

            if (
                uiState.errorMessage != null &&
                uiState.entityTypes.isEmpty() &&
                !uiState.isLoading
            ) {

                ErrorContent(

                    message =
                        uiState.errorMessage
                            .orEmpty(),

                    onRetry = {
                        viewModel.loadEntityTypes()
                    }
                )
            }

            // =================================================
            // LOADING INICIAL
            // =================================================

            else if (
                uiState.isLoading &&
                uiState.entityTypes.isEmpty()
            ) {

                LoadingContent()
            }

            // =================================================
            // LISTA VACÍA
            // =================================================

            else if (
                !uiState.isLoading &&
                uiState.entityTypes.isEmpty() &&
                uiState.errorMessage == null
            ) {

                EmptyContent(

                    onCreate = {

                        viewModel.startCreate()

                        onCreate()
                    }
                )
            }

            // =================================================
            // LISTADO
            // =================================================

            else {

                // ---------------------------------------------
                // Error mientras todavía hay datos
                // ---------------------------------------------

                if (
                    uiState.errorMessage != null &&
                    uiState.entityTypes.isNotEmpty()
                ) {

                    Text(

                        text =
                            uiState.errorMessage
                                .orEmpty(),

                        color =
                            MaterialTheme
                                .colorScheme
                                .error,

                        style =
                            MaterialTheme
                                .typography
                                .bodySmall,

                        modifier =
                            Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 4.dp
                            )
                    )
                }

                // ---------------------------------------------
                // Sin coincidencias de búsqueda
                // ---------------------------------------------

                if (
                    searchQuery.isNotBlank() &&
                    filteredEntityTypes.isEmpty()
                ) {

                    Box(

                        modifier =
                            Modifier.fillMaxSize(),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text =
                                "No se encontraron coincidencias."
                        )
                    }

                } else {

                    // -----------------------------------------
                    // LISTA
                    // -----------------------------------------

                    LazyColumn(

                        modifier =
                            Modifier.fillMaxSize(),

                        contentPadding =
                            PaddingValues(
                                top = 4.dp,
                                bottom = 100.dp
                            )
                    ) {

                        items(

                            items =
                                filteredEntityTypes,

                            key = {
                                it.idEntityType
                            }

                        ) { entityType ->

                            EntityTypeItem(

                                entityType =
                                    entityType,

                                onEdit = { id ->
                                    onEdit(id)
                                },

                                onDelete = {
                                    viewModel.requestDelete(it)
                                },

                                enabled =
                                    !uiState.isDeleting &&
                                            !uiState.isSaving
                            )
                        }
                    }
                }
            }
        }
    }

    // =========================================================
    // CONFIRMACIÓN DE ELIMINACIÓN
    // =========================================================

    val entityTypeToDelete =
        uiState.entityTypeToDelete

    if (entityTypeToDelete != null) {

        AlertDialog(

            onDismissRequest = {

                if (!uiState.isDeleting) {

                    viewModel.cancelDelete()
                }
            },

            title = {

                Text(
                    text =
                        "Eliminar tipo de entidad"
                )
            },

            text = {

                Text(
                    text =
                        "¿Seguro que querés eliminar " +
                                "\"${entityTypeToDelete.nameTypeEntity}\"?"
                )
            },

            confirmButton = {

                Button(

                    onClick = {
                        viewModel.deleteEntityType()
                    },

                    enabled =
                        !uiState.isDeleting
                ) {

                    if (uiState.isDeleting) {

                        Row(

                            horizontalArrangement =
                                Arrangement.Center,

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            CircularProgressIndicator(

                                modifier =
                                    Modifier.size(18.dp),

                                strokeWidth = 2.dp
                            )

                            Spacer(
                                modifier =
                                    Modifier.size(8.dp)
                            )

                            Text(
                                text =
                                    "Eliminando..."
                            )
                        }

                    } else {

                        Text(
                            text = "Eliminar"
                        )
                    }
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        viewModel.cancelDelete()
                    },

                    enabled =
                        !uiState.isDeleting
                ) {

                    Text(
                        text = "Cancelar"
                    )
                }
            }
        )
    }
}


// =============================================================
// LOADING
// =============================================================

@Composable
private fun LoadingContent() {

    Box(

        modifier =
            Modifier.fillMaxSize(),

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
                    "Cargando tipos de entidad..."
            )
        }
    }
}


// =============================================================
// ERROR
// =============================================================

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {

    Box(

        modifier =
            Modifier.fillMaxSize(),

        contentAlignment =
            Alignment.Center
    ) {

        Column(

            horizontalAlignment =
                Alignment.CenterHorizontally,

            modifier =
                Modifier.padding(24.dp)
        ) {

            Text(

                text =
                    "No se pudieron cargar los datos.",

                style =
                    MaterialTheme
                        .typography
                        .titleMedium,

                color =
                    MaterialTheme
                        .colorScheme
                        .error
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = message
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Button(
                onClick = onRetry
            ) {

                Text(
                    text = "Reintentar"
                )
            }
        }
    }
}


// =============================================================
// EMPTY
// =============================================================

@Composable
private fun EmptyContent(
    onCreate: () -> Unit
) {

    Box(

        modifier =
            Modifier.fillMaxSize(),

        contentAlignment =
            Alignment.Center
    ) {

        Column(

            horizontalAlignment =
                Alignment.CenterHorizontally,

            modifier =
                Modifier.padding(24.dp)
        ) {

            Text(

                text =
                    "No existen tipos de entidad.",

                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Podés crear el primero desde el botón inferior."
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Button(
                onClick = onCreate
            ) {

                Text(
                    text =
                        "Crear tipo de entidad"
                )
            }
        }
    }
}