package io.app.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Message(
    @SerialName("message") val message: String,
)
