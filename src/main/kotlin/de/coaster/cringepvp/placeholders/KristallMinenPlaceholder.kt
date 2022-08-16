package de.coaster.cringepvp.placeholders

import de.coaster.cringepvp.extensions.getCooldown
import de.coaster.cringepvp.extensions.toCringeLocation
import de.coaster.cringepvp.listeners.GamemodeListeners
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class KristallMinenPlaceholder : PlaceholderExpansion() {

    override fun getIdentifier(): String {
       return "kristallmine"
    }

    override fun getAuthor(): String {
        return "CoasterFreakDE"
    }

    override fun getVersion(): String {
        return "1.0"
    }

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        val kristallString = StringBuilder("#9b59b6")
        for (i in 1 .. GamemodeListeners.kristallFuellstand) {
            kristallString.append("▀ ")
        }
        kristallString.append("#95a5a6")
        for (i in GamemodeListeners.kristallFuellstand + 1 .. 10) {
            kristallString.append("▀ ")
        }

        return kristallString.toString()
    }
}