package com.jairllalen.reclamos_android.ui.entitytype

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jairllalen.reclamos_android.data.model.EntityType

@Composable
fun EntityTypeItem(
    entityType: EntityType,
    onEdit: (Int) -> Unit,
    onDelete: (EntityType) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = entityType.nameTypeEntity,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "ID: ${entityType.idEntityType}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Tamaño: ${entityType.entitySize}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Sector: ${entityType.sector}",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    onClick = {
                        onEdit(entityType.idEntityType)
                    },
                    enabled = enabled
                ) {
                    Text("Editar")
                }

                TextButton(
                    onClick = {
                        onDelete(entityType)
                    },
                    enabled = enabled
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}