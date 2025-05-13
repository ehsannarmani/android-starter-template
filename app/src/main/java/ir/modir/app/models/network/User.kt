package ir.modir.app.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class User(
    @SerialName("email")
    val email: String
)
