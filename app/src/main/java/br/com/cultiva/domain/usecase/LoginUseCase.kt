package br.com.cultiva.domain.usecase

class LoginUseCase {
    operator fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Preencha e-mail e senha."))
        }
        if (!email.contains("@") || !email.contains(".")) {
            return Result.failure(IllegalArgumentException("E-mail inválido."))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("A senha deve ter ao menos 6 caracteres."))
        }
        return Result.success(Unit)
    }
}
