package clients.telegram

import com.fasterxml.jackson.annotation.JsonAlias

data class MessageRequest(
    @JsonAlias("chat_id")
    val chatId: String,

    val text: String
)
