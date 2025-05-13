package ir.modir.app.viewModels

import androidx.datastore.core.DataStore
import ir.modir.app.models.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppSession : KoinComponent {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val userPreferences: DataStore<UserPreferences> by inject()

    val user = userPreferences.data.stateIn()

    private fun <T> Flow<T>.stateIn() = stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
}