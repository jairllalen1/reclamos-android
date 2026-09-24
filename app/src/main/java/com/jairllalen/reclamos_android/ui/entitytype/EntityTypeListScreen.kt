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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jairllalen.reclamos_android.data.model.EntityType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityTypeListScreen(
    onCreate: () -> Unit,
    onEdit: (Int) -> Unit,
    viewModel: EntityTypeViewModel = viewModel(
        factory = EntityTypeViewModelFactory()
    )
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchText by remember {
        mutableStateOf("")
    }

    var entityToDelete by remember {
        mutableStateOf<EntityType?>(null)
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    // ============================================================
    // CARGA INICIAL / REGRESO A LA PANTALLA
    // ============================================================

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {

        if (!uiState.isLoading) {
            viewModel.loadEntities()
        }
    }

    // ============================================================
    // MENSAJES DE ÉXITO
    // ============================================================

    LaunchedEffect(uiState.deleteSuccess) {

        if (uiState.deleteSuccess) {

            snackbarHostState.showSnackbar(
                "Registro eliminado correctamente."
            )

            viewModel.clearDeleteMessage()
        }
    }

    // ============================================================
    // FILTRADO
    // ============================================================

    val filteredEntities = uiState.entities.filter { entity ->

        val query = searchText.trim()

        if (query.isEmpty()) {
            true
        } else {

            entity.idEntityType.toString()
                .contains(query, ignoreCase = true) ||

                    entity.nameTypeEntity.contains(
                        query,
                        ignoreCase = true
                    ) ||

                    entity.entitySize.contains(
                        query,
                        ignoreCase = true
                    ) ||

                    entity.sector.contains(
                        query,
                        ignoreCase = true
                    )
        }
    }

    // ============================================================
    // PANTALLA PRINCIPAL
    // ============================================================

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Tipos de entidad")
                },

                actions = {

                    IconButton(
                        onClick = {
                            viewModel.loadEntities()
                        },
                        enabled = !uiState.isLoading
                    ) {

                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar"
                        )
                    }
                }
            )
        },

        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },

        floatingActionButton = {

            FloatingActionButton(
                onClick = onCreate
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear"
                )
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // ====================================================
            // BARRA DE BÚSQUEDA
            // ====================================================

            androidx.compose.material3.OutlinedTextField(

                value = searchText,

                onValueChange = {
                    searchText = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                label = {
                    Text("Buscar")
                },

                singleLine = true
            )

            // ====================================================
            // ERROR CON DATOS EXISTENTES
            // ====================================================

            if (
                uiState.errorMessage != null &&
                uiState.entities.isNotEmpty()
            ) {

                ErrorBanner(
                    message = uiState.errorMessage!!,
                    onRetry = {
                        viewModel.loadEntities()
                    }
                )
            }

            // ====================================================
            // LOADING
            // ====================================================

            if (
                uiState.isLoading &&
                uiState.entities.isEmpty()
            ) {

                LoadingContent()

            }

            // ====================================================
            // ERROR SIN DATOS
            // ====================================================

            else if (
                !uiState.isLoading &&
                uiState.errorMessage != null &&
                uiState.entities.isEmpty()
            ) {

                ErrorContent(
                    message = uiState.errorMessage!!,
                    onRetry = {
                        viewModel.loadEntities()
                    }
                )

            }

            // ====================================================
            // LISTA VACÍA
            // ====================================================

            else if (
                !uiState.isLoading &&
                uiState.errorMessage == null &&
                uiState.entities.isEmpty()
            ) {

                EmptyContent()

            }

            // ====================================================
            // BÚSQUEDA SIN RESULTADOS
            // ====================================================

            else if (
                uiState.entities.isNotEmpty() &&
                filteredEntities.isEmpty()
            ) {

                NoSearchResults()

            }

            // ====================================================
            // LISTA
            // ====================================================

            else {

                EntityTypeList(
                    entities = filteredEntities,
                    onEdit = onEdit,
                    onDelete = {
                        entityToDelete = it
                    }
                )
            }
        }
    }

    // ============================================================
    // CONFIRMACIÓN DELETE
    // ============================================================

    entityToDelete?.let { entity ->

        AlertDialog(

            onDismissRequest = {

                if (!uiState.isDeleting) {
                    entityToDelete = null
                }
            },

            title = {
                Text("Eliminar tipo de entidad")
            },

            text = {
                Text(
                    "¿Seguro que querés eliminar " +
                            "\"${entity.nameTypeEntity}\"?"
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        viewModel.deleteEntity(
                            entity.idEntityType
                        )

                        entityToDelete = null
                    },

                    enabled = !uiState.isDeleting

                ) {

                    if (uiState.isDeleting) {

                        CircularProgressIndicator(
                            modifier = Modifier.height(20.dp),
                            strokeWidth = 2.dp
                        )

                    } else {

                        Text("Eliminar")
                    }
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        entityToDelete = null
                    },

                    enabled = !uiState.isDeleting

                ) {

                    Text("Cancelar")
                }
            }
        )
    }

    // ============================================================
    // ERROR DELETE
    // ============================================================

    uiState.deleteErrorMessage?.let { message ->

        LaunchedEffect(message) {

            snackbarHostState.showSnackbar(message)

            viewModel.clearDeleteMessage()
        }
    }
}


// =================================================================
// LISTA
// =================================================================

@Composable
private fun EntityTypeList(
    entities: List<EntityType>,
    onEdit: (Int) -> Unit,
    onDelete: (EntityType) -> Unit
) {

    LazyColumn(

        modifier = Modifier.fillMaxSize(),

        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        ),

        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {

        items(
            items = entities,
            key = {
                it.idEntityType
            }

        ) { entity ->

            EntityTypeItem(

                entityType = entity,

                onEdit = {
                    onEdit(entity.idEntityType)
                },

                onDelete = {
                    onDelete(entity)
                }
            )
        }
    }
}


// =================================================================
// LOADING
// =================================================================

@Composable
private fun LoadingContent() {

    Box(
        modifier = Modifier.fillMaxSize(),
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
                text = "Cargando tipos de entidad..."
            )
        }
    }
}


// =================================================================
// EMPTY
// =================================================================

@Composable
private fun EmptyContent() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "No hay tipos de entidad."
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "La lista está vacía."
            )
        }
    }
}


// =================================================================
// SIN RESULTADOS DE BÚSQUEDA
// =================================================================

@Composable
private fun NoSearchResults() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "No se encontraron resultados."
        )
    }
}


// =================================================================
// ERROR
// =================================================================

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                text = "No se pudieron cargar los datos.",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = message
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onRetry
            ) {

                Text("Reintentar")
            }
        }
    }
}


// =================================================================
// ERROR BANNER
// =================================================================

@Composable
private fun ErrorBanner(
    message: String,
    onRetry: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = message,
            modifier = Modifier.weight(1f)
        )

        TextButton(
            onClick = onRetry
        ) {

            Text("Reintentar")
        }
    }
}