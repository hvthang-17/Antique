package com.example.antique.model.remote.entity

data class SendGridMailBody(
    val personalizations: List<Personalization>,
    val from: Email,
    val content: List<Content>
)

data class Personalization(val to: List<Email>)
data class Email(val email: String)
data class Content(val type: String = "text/plain", val value: String)

