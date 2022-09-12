package de.coaster.cringepvp.placeholders

import de.coaster.cringepvp.extensions.toCringeUser
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class SteinbruchPlaceholder : PlaceholderExpansion() {

    override fun getIdentifier(): String {
       return "steinbruch"
    }

    override fun getAuthor(): String {
        return "CoasterFreakDE"
    }

    override fun getVersion(): String {
        return "1.0"
    }

    override fun onRequest(player: OfflinePlayer, params: String): String {
        if (!player.isOnline) {
            return "§cError"
        }
        val cringeUser = player.uniqueId.toCringeUser()

        if(params.equals("level", true)) {
            return cringeUser.steinbruchLevel.toString()
        }
        return "${cringeUser.steinBruchIdle} / s"
    }
}