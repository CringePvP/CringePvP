package de.coaster.cringepvp.placeholders

import de.coaster.cringepvp.extensions.getCooldown
import de.coaster.cringepvp.extensions.toCringeLocation
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class CratePlaceholders : PlaceholderExpansion() {

    override fun getIdentifier(): String {
       return "cooldown"
    }

    override fun getAuthor(): String {
        return "CoasterFreakDE"
    }

    override fun getVersion(): String {
        return "1.0"
    }

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        val location = params.toCringeLocation()

        if(location.getCooldown() != null) {
            val cooldown = location.getCooldown()!!
            if (cooldown.isNegative()) {
                return "Bereit"
            }
            return location.getCooldown()?.inWholeSeconds?.seconds.toString()
        }

        return null
    }
}