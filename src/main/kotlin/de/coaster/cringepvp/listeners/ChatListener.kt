package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.extensions.toCringeUser
import dev.fruxz.stacked.extension.asPlainString
import dev.fruxz.stacked.text
import io.papermc.paper.event.player.AsyncChatEvent
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

class ChatListener : Listener {

    private val chatFormat = "<color:%color%>%rank% <color:#3d3d3d>»</color> <color:#c8d6e5>%playername%</color> <color:#f6e58d>"

    private val urlRegex =
        Regex("http[s]?:\\/\\/(?:[a-zA-Z]|[0-9]|[\$-_@.&+]|[!*\\(\\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+")

    private val chatBypassPermission = Permission("cringepvp.utility.chat.bypass", PermissionDefault.OP)

    @EventHandler
    fun onChat(event: AsyncChatEvent): Unit = with(event) {
        val player = player

        val placeHolderText = chatFormat
        val messagePlain = message().asPlainString

        val format =
            text(parsePlaceholders(placeHolderText, player)).append(text(parsePlaceholders(messagePlain, player)))

        event.renderer { _, _, _, _ ->
            return@renderer format
        }
    }

    private fun parsePlaceholders(text: String, player: Player): String {
        var parsed = text
        parsed = parsed.replace("%playername%", player.name)
        parsed = parsed.replace("%displayname%", player.displayName().asPlainString)
        parsed = parsed.replace("%color%", player.toCringeUser().rank.color)
        parsed = parsed.replace("%rank%", player.toCringeUser().rank.name)

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            parsed = PlaceholderAPI.setPlaceholders(player, parsed)
        }

        val plainText = text(parsed).asPlainString

        // Test for link and replace it with a clickable link
        for (match in urlRegex.findAll(plainText)) {
            val url = match.value
            val urlText = "<color:#7593ff><click:open_url:'$url'>$url</click></color>"
            parsed = parsed.replace(url, urlText)
        }
        return parsed
    }

}