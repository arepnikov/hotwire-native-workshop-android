package com.example.hotwirenativeworkshop

import android.app.Application
import com.example.hotwirenativeworkshop.bridge.ButtonComponent
import com.example.hotwirenativeworkshop.features.web.WebBottomSheetFragment
import com.example.hotwirenativeworkshop.features.web.WebFragment
import dev.hotwire.core.BuildConfig
import dev.hotwire.core.bridge.BridgeComponentFactory
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration
import dev.hotwire.navigation.config.defaultFragmentDestination
import dev.hotwire.navigation.config.registerBridgeComponents
import dev.hotwire.navigation.config.registerFragmentDestinations


class KanbanBoardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        configureApp()
    }

    private fun configureApp() {
        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                assetFilePath = "json/path-configuration.json",
                remoteFileUrl = "${KanbanBoard.current.url}/configurations/android_v1.json"
            )
        )

        // Set the default fragment destination
        Hotwire.defaultFragmentDestination = WebFragment::class

        // Register fragment destinations
        Hotwire.registerFragmentDestinations(
            WebFragment::class,
            WebBottomSheetFragment::class
        )

        // Register bridge components
        Hotwire.registerBridgeComponents(
            BridgeComponentFactory("button", ::ButtonComponent)
        )

        // Set configuration options
        Hotwire.config.debugLoggingEnabled = BuildConfig.DEBUG
        Hotwire.config.webViewDebuggingEnabled = BuildConfig.DEBUG
        Hotwire.config.jsonConverter = KotlinXJsonConverter()
        Hotwire.config.applicationUserAgentPrefix = "Kanban Boards;"
    }
}
