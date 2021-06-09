package tech.danielwaiguru.dscfirebaseauth.domain.repository

import com.google.firebase.auth.AuthResult
import tech.danielwaiguru.dscfirebaseauth.data.model.User
import tech.danielwaiguru.dscfirebaseauth.domain.model.UserDomain
import tech.danielwaiguru.dscfirebaseauth.util.ResultWrapper

interface AuthRepo {
    suspend fun signUp(user: User): ResultWrapper<AuthResult>
    suspend fun signIn(email: String, password: String): ResultWrapper<AuthResult>
    suspend fun sendResetToken(email: String): ResultWrapper<Void?>
    suspend fun saveUser(uid:String, user: UserDomain)
}