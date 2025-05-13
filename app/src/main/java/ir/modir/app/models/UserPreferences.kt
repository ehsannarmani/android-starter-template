package ir.modir.app.models

import ir.modir.app.models.network.User
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val loggedIn: Boolean,
    val user: User,
    val authorizationToken: String
)