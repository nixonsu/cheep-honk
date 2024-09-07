package com.nixonsu.cheephonk.clients.telegram

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageRequest(
    @JsonProperty("chat_id")
    val chatId: String,

    val text: String,

    @JsonProperty("parse_mode")
    val parseMode: String? = null,

    @JsonProperty("link_preview_options")
    val linkPreviewOptions: LinkPreviewOptions? = null,
)

data class LinkPreviewOptions(
    @JsonProperty("is_disabled")
    val isDisabled: Boolean? = null,
)
