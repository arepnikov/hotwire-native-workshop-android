package com.example.hotwirenativeworkshop

import android.app.Application
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration

class KanbanBoardApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                assetFilePath = "json/path-configuration.json"
            )
        )
    }
}
