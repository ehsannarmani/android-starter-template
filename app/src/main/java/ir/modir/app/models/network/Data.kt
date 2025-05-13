package ir.modir.app.models.network

import kotlinx.serialization.Serializable

@Serializable
data class Data<T>(val data: T)
