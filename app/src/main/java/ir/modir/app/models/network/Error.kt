package ir.modir.app.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Error(
    @SerialName("message")
    val message: String,
    @SerialName("errors")
    val errors: String?
)
