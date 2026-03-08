package com.example.hotwirenativeworkshop.bridge

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.R
import dev.hotwire.navigation.destinations.HotwireDestination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class FormComponent(
    name: String,
    private val delegate: BridgeDelegate<HotwireDestination>
) : BridgeComponent<HotwireDestination>(name, delegate) {

    private val submitButtonItemId = 38
    private var submitMenuItem: MenuItem? = null

    private val fragment: Fragment
        get() = delegate.destination.fragment

    private val toolbar: Toolbar?
        get() = fragment.view?.findViewById(R.id.toolbar)

    override fun onReceive(message: Message) {
        // Handle incoming messages based on the message `event`.
        when (message.event) {
            "connect" -> handleConnectEvent(message)
            "submitEnabled" -> handleSubmitEnabled()
            "submitDisabled" -> handleSubmitDisabled()
            else -> Log.w("FormComponent", "Unknown event for message: $message")
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

        menu.removeItem(submitButtonItemId)  // Remove any existing button first (prevents duplicates)
        submitMenuItem = menu.add(Menu.NONE, submitButtonItemId, order, title).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)  // Always show in toolbar, not overflow
            setOnMenuItemClickListener {
                performSubmit()
                true
            }
        }
    }

    private fun handleSubmitEnabled() {
        toggleSubmitButton(true)
    }

    private fun handleSubmitDisabled() {
        toggleSubmitButton(false)
    }

    private fun toggleSubmitButton(enable: Boolean) {
        submitMenuItem?.isEnabled = enable
    }

    private fun performSubmit(): Boolean {
        return replyTo("connect")
    }

    // Use kotlinx.serialization annotations to define a serializable
    // data class that represents the incoming message.data json.
    @Serializable
    data class MessageData(
        @SerialName("submitTitle") val title: String
    )
}