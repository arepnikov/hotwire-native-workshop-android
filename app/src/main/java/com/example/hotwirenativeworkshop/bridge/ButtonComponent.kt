package com.example.hotwirenativeworkshop.bridge

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import dev.hotwire.navigation.R
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.core.graphics.toColorInt

class ButtonComponent(
    name: String,
    private val delegate: BridgeDelegate<HotwireDestination>
) : BridgeComponent<HotwireDestination>(name, delegate) {

    // A unique integer ID for your menu item (any number, just needs to be unique)
    private val buttonItemId = 37

    // Keep a reference to the menu item so you can manipulate it later
    private var buttonMenuItem: MenuItem? = null

    // Get the Fragment that hosts this screen
    private val fragment: Fragment
        get() = delegate.destination.fragment

    // Find the Toolbar view inside that Fragment
    private val toolbar: Toolbar?
        get() = fragment.view?.findViewById(R.id.toolbar)

    override fun onReceive(message: Message) {
        // Handle incoming messages based on the message `event`.
        when (message.event) {
            "connect" -> handleConnectEvent(message)
            else -> Log.w("ButtonComponent", "Unknown event for message: $message")
        }
    }

    private fun handleConnectEvent(message: Message) {
        val data = message.data<MessageData>() ?: return
        showToolbarButton(data)
    }

    private fun showToolbarButton(data: MessageData) {
        val menu = toolbar?.menu ?: return
        val order = 999 // Show as the right-most button

        val title = SpannableString(data.title).apply {
            setSpan(ForegroundColorSpan("#FF6600".toColorInt()), 0, length, 0)
        }

        menu.removeItem(buttonItemId)  // Remove any existing button first (prevents duplicates)
        buttonMenuItem = menu.add(Menu.NONE, buttonItemId, order, title).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)  // Always show in toolbar, not overflow
            setOnMenuItemClickListener {
                performButtonClick()  // Wire the tap to reply back to JS
                true
            }
        }
    }

    private fun performButtonClick(): Boolean {
        return replyTo("connect")
    }

    // Use kotlinx.serialization annotations to define a serializable
    // data class that represents the incoming message.data json.
    @Serializable
    data class MessageData(
        @SerialName("title") val title: String
    )
}

