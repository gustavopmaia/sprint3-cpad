package br.com.cultiva.navigation

object Routes {
    const val LOGIN = "login"
    const val ORDERS = "orders"
    const val HISTORY = "history"
    const val DETAIL = "detail/{orderId}"

    fun detail(orderId: Int) = "detail/$orderId"
}
