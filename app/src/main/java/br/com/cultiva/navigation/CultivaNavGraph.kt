package br.com.cultiva.navigation

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.cultiva.di.AppContainer
import br.com.cultiva.domain.model.UiState
import br.com.cultiva.ui.login.LoginScreen
import br.com.cultiva.ui.login.LoginViewModel
import br.com.cultiva.ui.orders.DetailScreen
import br.com.cultiva.ui.orders.HistoryScreen
import br.com.cultiva.ui.orders.OrdersScreen
import br.com.cultiva.ui.orders.OrdersViewModel

@Composable
fun CultivaNavGraph(container: AppContainer, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val snackbar = remember { SnackbarHostState() }

    val ordersViewModel: OrdersViewModel = viewModel(
        factory = OrdersViewModel.Factory(
            container.getOrders, container.startOrder, container.finishOrder,
            container.addEvidence, container.seedOrders
        )
    )

    val ordersState by ordersViewModel.ordersState.collectAsStateWithLifecycle()
    val message by ordersViewModel.message.collectAsStateWithLifecycle()

    LaunchedEffect(message) {
        message?.let {
            snackbar.showSnackbar(it)
            ordersViewModel.consumeMessage()
        }
    }

    androidx.compose.material3.Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = if (container.session.loggedIn) Routes.ORDERS else Routes.LOGIN,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.LOGIN) {
                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModel.Factory(container.loginUseCase, container.session)
                )
                LoginScreen(loginViewModel) {
                    navController.navigate(Routes.ORDERS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }

            composable(Routes.ORDERS) {
                OrdersScreen(
                    state = ordersState,
                    onOrder = { navController.navigate(Routes.detail(it.id)) },
                    onHistory = { navController.navigate(Routes.HISTORY) }
                )
            }

            composable(Routes.HISTORY) {
                HistoryScreen(
                    state = ordersState,
                    onOrders = { navController.navigate(Routes.ORDERS) { popUpTo(Routes.ORDERS) { inclusive = true } } }
                )
            }

            composable(
                Routes.DETAIL,
                arguments = listOf(navArgument("orderId") { type = NavType.IntType })
            ) { entry ->
                val orderId = entry.arguments?.getInt("orderId") ?: return@composable
                val order = (ordersState as? UiState.Success)?.data?.firstOrNull { it.id == orderId }
                if (order == null) {
                    navController.popBackStack()
                    return@composable
                }
                DetailScreen(
                    order = order,
                    onBack = { navController.popBackStack(Routes.ORDERS, inclusive = false) },
                    onHistory = { navController.navigate(Routes.HISTORY) },
                    onEvidence = { ordersViewModel.evidence(order) },
                    onStart = { ordersViewModel.start(order) },
                    onFinish = {
                        ordersViewModel.finish(order)
                        navController.popBackStack(Routes.ORDERS, inclusive = false)
                    }
                )
            }
        }
    }
}
