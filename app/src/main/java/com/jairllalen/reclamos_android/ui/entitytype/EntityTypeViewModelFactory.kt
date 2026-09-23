package com.jairllalen.reclamos_android.ui.entitytype

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jairllalen.reclamos_android.data.repository.EntityTypeRepository

class EntityTypeViewModelFactory(
    private val repository: EntityTypeRepository = EntityTypeRepository()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(EntityTypeViewModel::class.java)) {
            return EntityTypeViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}