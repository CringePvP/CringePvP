package de.coaster.cringepvp.placeholders

import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.extensions.toCringeUser
import me.neznamy.tab.api.TabAPI
import org.bukkit.entity.Player

val cratePlaceholder = CratePlaceholders()
val kristallMinenPlaceholder = KristallMinenPlaceholder()

fun registerPlaceholders() {

    val placeholderManager = TabAPI.getInstance().placeholderManager

    placeholderManager.registerPlayerPlaceholder("%kills%", 1000) { player ->
        (player.player as Player).toCringeUser().kills
    }

    placeholderManager.registerPlayerPlaceholder("%deaths%", 1000) { player ->
        (player.player as Player).toCringeUser().deaths
    }

    placeholderManager.registerPlayerPlaceholder("%rank%", 5000) { player ->
        (player.player as Player).toCringeUser().rank
    }

    placeholderManager.registerPlayerPlaceholder("%title%", 2000) { player ->
        Titles.values().find { it.name.equals((player.player as Player).toCringeUser().title, true) }?.display ?: "Neuer Noob"
    }

    placeholderManager.registerPlayerPlaceholder("%color%", 5000) { player ->
        Ranks.values().find { it.name.equals((player.player as Player).toCringeUser().rank, true) }?.color ?: "#7bed9f"
    }

    placeholderManager.registerPlayerPlaceholder("%coins%", 1000) { player ->
        (player.player as Player).toCringeUser().coins
    }

    placeholderManager.registerPlayerPlaceholder("%gems%", 1000) { player ->
        (player.player as Player).toCringeUser().gems
    }

    placeholderManager.registerPlayerPlaceholder("%crystals%", 1000) { player ->
        (player.player as Player).toCringeUser().crystals
    }

    placeholderManager.registerPlayerPlaceholder("%relicts%", 1000) { player ->
        (player.player as Player).toCringeUser().relicts
    }

    placeholderManager.registerPlayerPlaceholder("%xp%", 1000) { player ->
        (player.player as Player).toCringeUser().xp
    }

    placeholderManager.registerPlayerPlaceholder("%level%", 1000) { player ->
        (player.player as Player).toCringeUser().level
    }

    placeholderManager.registerPlayerPlaceholder("%nextLevelExp%", 1000) { player ->
        (player.player as Player).toCringeUser().nextLevelExp()
    }


    cratePlaceholder.register()
    kristallMinenPlaceholder.register()
}

fun unregisterPlaceholders() {
    cratePlaceholder.unregister()
    kristallMinenPlaceholder.unregister()
}