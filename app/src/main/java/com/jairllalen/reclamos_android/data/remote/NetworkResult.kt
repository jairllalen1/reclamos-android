package com.jairllalen.reclamos_android.data.remote

sealed interface NetworkResult<out T> {

    /**
     * La petición fue exitosa.
     */
    data class Success<T>(
        val data: T
    ) : NetworkResult<T>

    /**
     * La API respondió, pero con un código HTTP de error.
     */
    data class HttpError(
        val code: Int,
        val message: String? = null
    ) : NetworkResult<Nothing>

    /**
     * No se pudo establecer la comunicación con el servidor.
     */
    data class NetworkError(
        val exception: Exception
    ) : NetworkResult<Nothing>

    /**
     * Error inesperado que no corresponde a los casos anteriores.
     */
    data class UnknownError(
        val exception: Exception
    ) : NetworkResult<Nothing>
}