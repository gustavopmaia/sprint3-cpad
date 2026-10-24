package br.com.cultiva.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cultiva.data.preferences.SessionPreferences
import br.com.cultiva.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val session: SessionPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    val savedEmail: String get() = session.lastEmail

    fun login(email: String, password: String) {
        _state.value = LoginUiState(loading = true)
        val result = loginUseCase(email.trim(), password)
        _state.value = result.fold(
            onSuccess = {
                session.loggedIn = true
                session.lastEmail = email.trim()
                LoginUiState(success = true)
            },
            onFailure = { LoginUiState(error = it.message ?: "Falha ao entrar.") }
        )
    }

    fun consumeError() {
        _state.value = _state.value.copy(error = null)
    }

    class Factory(
        private val loginUseCase: LoginUseCase,
        private val session: SessionPreferences
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LoginViewModel(loginUseCase, session) as T
    }
}
