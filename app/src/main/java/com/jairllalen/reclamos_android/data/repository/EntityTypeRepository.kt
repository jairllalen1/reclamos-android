package com.jairllalen.reclamos_android.data.repository

import com.google.gson.Gson
import com.jairllalen.reclamos_android.data.model.ApiError
import com.jairllalen.reclamos_android.data.model.EntityType
import com.jairllalen.reclamos_android.data.model.EntityTypeCreateRequest
import com.jairllalen.reclamos_android.data.model.EntityTypeUpdateRequest
import com.jairllalen.reclamos_android.data.remote.ApiClient
import com.jairllalen.reclamos_android.data.remote.ApiService
import com.jairllalen.reclamos_android.data.remote.NetworkResult
import retrofit2.Response
import java.io.IOException

class EntityTypeRepository(
    private val apiService: ApiService = ApiClient.apiService
) {

    private val gson = Gson()

    /**
     * Obtiene todos los tipos de entidad.
     *
     * GET /api/entity-types
     */
    suspend fun getAllEntityTypes(): NetworkResult<List<EntityType>> {
        return executeRequest {
            apiService.getAllEntityTypes()
        }
    }

    /**
     * Obtiene un tipo de entidad por ID.
     *
     * GET /api/entity-types/{id}
     */
    suspend fun getEntityTypeById(id: Int): NetworkResult<EntityType> {
        return executeRequest {
            apiService.getEntityTypeById(id)
        }
    }

    /**
     * Crea un nuevo tipo de entidad.
     *
     * POST /api/entity-types
     */
    suspend fun createEntityType(
        request: EntityTypeCreateRequest
    ): NetworkResult<EntityType> {
        return executeRequest {
            apiService.createEntityType(request)
        }
    }

    /**
     * Actualiza un tipo de entidad existente.
     *
     * PUT /api/entity-types/{id}
     */
    suspend fun updateEntityType(
        id: Int,
        request: EntityTypeUpdateRequest
    ): NetworkResult<EntityType> {
        return executeRequest {
            apiService.updateEntityType(id, request)
        }
    }

    /**
     * Elimina un tipo de entidad.
     *
     * DELETE /api/entity-types/{id}
     *
     * El backend responde 204 No Content.
     */
    suspend fun deleteEntityType(id: Int): NetworkResult<Unit> {

        return try {

            val response = apiService.deleteEntityType(id)

            if (response.isSuccessful) {

                NetworkResult.Success(Unit)

            } else {

                NetworkResult.HttpError(
                    code = response.code(),
                    message = extractErrorMessage(response)
                )
            }

        } catch (e: IOException) {

            NetworkResult.NetworkError(e)

        } catch (e: Exception) {

            NetworkResult.UnknownError(e)
        }
    }

    /**
     * Ejecuta una petición HTTP que esperamos que devuelva un cuerpo.
     */
    private suspend fun <T> executeRequest(
        request: suspend () -> Response<T>
    ): NetworkResult<T> {

        return try {

            val response = request()

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {

                    NetworkResult.Success(body)

                } else {

                    NetworkResult.UnknownError(
                        IllegalStateException(
                            "La API respondió correctamente pero no devolvió datos."
                        )
                    )
                }

            } else {

                NetworkResult.HttpError(
                    code = response.code(),
                    message = extractErrorMessage(response)
                )
            }

        } catch (e: IOException) {

            NetworkResult.NetworkError(e)

        } catch (e: Exception) {

            NetworkResult.UnknownError(e)
        }
    }

    /**
     * Intenta obtener el mensaje enviado por el backend.
     *
     * Siempre devuelve un String para poder usarlo
     * en NetworkResult.HttpError.
     */
    private fun <T> extractErrorMessage(
        response: Response<T>
    ): String {

        val errorBody = response.errorBody()?.string()

        if (errorBody.isNullOrBlank()) {
            return "Error HTTP ${response.code()}."
        }

        return try {

            val apiError = gson.fromJson(
                errorBody,
                ApiError::class.java
            )

            apiError.message ?: errorBody

        } catch (e: Exception) {

            errorBody
        }
    }
}