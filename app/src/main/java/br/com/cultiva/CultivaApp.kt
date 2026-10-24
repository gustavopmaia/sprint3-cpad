package br.com.cultiva

import android.app.Application
import br.com.cultiva.di.AppContainer

class CultivaApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
