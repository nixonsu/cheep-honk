package com.nixonsu.cheephonk.clients.telegram

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageRequest(
    @JsonProperty("chat_id")
    val chatId: String,
    val text: String
)
