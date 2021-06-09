package tech.danielwaiguru.dscfirebaseauth.data.model

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val cPassword: String
)