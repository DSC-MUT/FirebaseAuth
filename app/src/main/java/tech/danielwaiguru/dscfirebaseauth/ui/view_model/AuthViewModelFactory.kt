package tech.danielwaiguru.dscfirebaseauth.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.danielwaiguru.dscfirebaseauth.domain.repository.AuthRepo

class AuthViewModelFactory(private val authRepo: AuthRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            AuthViewModel(authRepo) as T
        } else throw IllegalArgumentException("Unknown AuthViewModel class")
    }
}