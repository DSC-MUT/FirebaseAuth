package tech.danielwaiguru.dscfirebaseauth.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import tech.danielwaiguru.dscfirebaseauth.data.mappers.toDomain
import tech.danielwaiguru.dscfirebaseauth.data.model.User
import tech.danielwaiguru.dscfirebaseauth.domain.model.UserDomain
import tech.danielwaiguru.dscfirebaseauth.domain.repository.AuthRepo
import tech.danielwaiguru.dscfirebaseauth.util.ResultWrapper

class AuthRepoImpl: AuthRepo {
    private val auth = Firebase.auth
    private val userCollection = Firebase.firestore.collection("users")
    override suspend fun signUp(user: User): ResultWrapper<AuthResult> =
        withContext(Dispatchers.IO) {
        try {
            val result = auth.createUserWithEmailAndPassword(
                user.email, user.password).await().also { authResult->
                authResult?.user?.uid?.let { uid->
                    saveUser(uid, user.toDomain())
                }
            }
            ResultWrapper.Success(result)
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "An error has occurred!")
        }
    }

    override suspend fun signIn(email: String, password: String): ResultWrapper<AuthResult> = withContext(Dispatchers.IO) {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            ResultWrapper.Success(result)
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "An error has occurred!")
        }
    }

    override suspend fun sendResetToken(email: String): ResultWrapper<Void?> =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.sendPasswordResetEmail(email).await()
                ResultWrapper.Success(result)
            } catch (e: Exception) {
                ResultWrapper.Error(e.message ?: "An error has occurred!")
            }
        }

    override suspend fun saveUser(uid:String, user: UserDomain){
        userCollection.document(uid).set(user).await()
    }
}