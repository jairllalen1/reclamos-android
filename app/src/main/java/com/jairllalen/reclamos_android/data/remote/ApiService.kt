package com.jairllalen.reclamos_android.data.remote

import com.jairllalen.reclamos_android.data.model.EntityType
import com.jairllalen.reclamos_android.data.model.EntityTypeCreateRequest
import com.jairllalen.reclamos_android.data.model.EntityTypeUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    /**
     * Obtiene todos los tipos de entidad.
     *
     * GET /api/entity-types
     */
    @GET("entity-types")
    suspend fun getAllEntityTypes(): Response<List<EntityType>>

    /**
     * Obtiene un tipo de entidad por ID.
     *
     * GET /api/entity-types/{id}
     */
    @GET("entity-types/{id}")
    suspend fun getEntityTypeById(
        @Path("id") id: Int
    ): Response<EntityType>

    /**
     * Crea un nuevo tipo de entidad.
     *
     * POST /api/entity-types
     */
    @POST("entity-types")
    suspend fun createEntityType(
        @Body request: EntityTypeCreateRequest
    ): Response<EntityType>

    /**
     * Actualiza un tipo de entidad existente.
     *
     * PUT /api/entity-types/{id}
     */
    @PUT("entity-types/{id}")
    suspend fun updateEntityType(
        @Path("id") id: Int,
        @Body request: EntityTypeUpdateRequest
    ): Response<EntityType>

    /**
     * Elimina un tipo de entidad.
     *
     * DELETE /api/entity-types/{id}
     */
    @DELETE("entity-types/{id}")
    suspend fun deleteEntityType(
        @Path("id") id: Int
    ): Response<Unit>
}