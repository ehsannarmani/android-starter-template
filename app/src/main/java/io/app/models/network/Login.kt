package io.app.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Login(
    @SerialName("user") val user: User,
    @SerialName("token") val token: String,
)
