package br.com.cultiva.di

import android.content.Context
import br.com.cultiva.data.local.AppDatabase
import br.com.cultiva.data.preferences.SessionPreferences
import br.com.cultiva.data.repository.OrderRepositoryImpl
import br.com.cultiva.domain.repository.OrderRepository
import br.com.cultiva.domain.usecase.AddEvidenceUseCase
import br.com.cultiva.domain.usecase.FinishOrderUseCase
import br.com.cultiva.domain.usecase.GetOrdersUseCase
import br.com.cultiva.domain.usecase.LoginUseCase
import br.com.cultiva.domain.usecase.SeedOrdersUseCase
import br.com.cultiva.domain.usecase.StartOrderUseCase

class AppContainer(context: Context) {

    val session: SessionPreferences = SessionPreferences(context)

    private val repository: OrderRepository =
        OrderRepositoryImpl(AppDatabase.getInstance(context).orderDao())

    val loginUseCase = LoginUseCase()
    val getOrders = GetOrdersUseCase(repository)
    val startOrder = StartOrderUseCase(repository)
    val finishOrder = FinishOrderUseCase(repository)
    val addEvidence = AddEvidenceUseCase(repository)
    val seedOrders = SeedOrdersUseCase(repository)
}
