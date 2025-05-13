package ir.modir.app.ui.utils

import androidx.annotation.StringRes
import ir.modir.app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

object UiMessenger {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Serializable
    data class Message(val content: String, val type: Type) {
        @Serializable
        enum class Type(@StringRes val title: Int) {
            INFO(title = R.string.snack_title_info),
            ERROR(title = R.string.snack_title_error),
            WARNING(title = R.string.snack_title_warning),
            SUCCESS(R.string.snack_title_success)
        }
    }

    val message = Channel<Message>()

    fun sendMessage(message: Message) {
        scope.launch {
            this@UiMessenger.message.send(message)
        }
    }

    fun sendError(error: String) {
        sendMessage(Message(content = error, type = Message.Type.ERROR))
    }

    fun sendSuccess(message: String) {
        sendMessage(Message(content = message, type = Message.Type.SUCCESS))
    }
}
