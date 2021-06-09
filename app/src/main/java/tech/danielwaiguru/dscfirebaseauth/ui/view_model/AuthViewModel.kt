package tech.danielwaiguru.dscfirebaseauth.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.launch
import tech.danielwaiguru.dscfirebaseauth.data.model.User
import tech.danielwaiguru.dscfirebaseauth.domain.repository.AuthRepo
import tech.danielwaiguru.dscfirebaseauth.extensions.isValidEmail
import tech.danielwaiguru.dscfirebaseauth.util.ResultWrapper

class AuthViewModel(private val authRepo: AuthRepo): ViewModel() {
    private val _registerStatus: MutableLiveData<ResultWrapper<AuthResult>> = MutableLiveData()
    val registerStatus: LiveData<ResultWrapper<AuthResult>> get() = _registerStatus

    private val _loginStatus: MutableLiveData<ResultWrapper<AuthResult>> = MutableLiveData()
    val loginStatus: LiveData<ResultWrapper<AuthResult>> get() = _loginStatus

    private val _resetLinkStatus: MutableLiveData<ResultWrapper<Void?>> = MutableLiveData()
    val resetLinkStatus: LiveData<ResultWrapper<Void?>> get() = _resetLinkStatus

    fun signUp(user: User) {
        val error = if (!user.email.isValidEmail()) {
            "Invalid email"
        } else if (user.firstName.isEmpty()){
            "First Name is required"
        } else if (user.lastName.isEmpty()) {
            "Last Name is required"
        } else if (user.cPassword != user.password) {
            "Password do not match"
        }
        else null
        error?.let {
            _registerStatus.value = ResultWrapper.Error(it)
            return
        }
        _registerStatus.value = ResultWrapper.Loading()
        viewModelScope.launch {
            _registerStatus.value = authRepo.signUp(user)
        }
    }
    fun signIn(email: String, password: String) {
        val error = if (!email.isValidEmail()) {
            "Invalid email"
        } else if (password.length < 6) {
            "Password too short"
        } else null
        error?.let {
            _loginStatus.value = ResultWrapper.Error(it)
            return
        }
        _loginStatus.value = ResultWrapper.Loading()
        viewModelScope.launch {
            _loginStatus.value = authRepo.signIn(email, password)
        }
    }
    fun sendResetPasswordLink(email: String) {
        val error = if (!email.isValidEmail()) {
            "Invalid email"
        } else null
        error?.let {
            _resetLinkStatus.value = ResultWrapper.Error(it)
            return
        }
        _resetLinkStatus.value = ResultWrapper.Loading()
        viewModelScope.launch {
            _resetLinkStatus.value = authRepo.sendResetToken(email)
        }
    }
}