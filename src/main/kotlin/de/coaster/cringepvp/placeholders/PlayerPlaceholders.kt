package de.coaster.cringepvp.placeholders

import de.coaster.cringepvp.extensions.toCringeUser
import me.neznamy.tab.api.TabAPI
import org.bukkit.entity.Player

val cratePlaceholder = CratePlaceholders()
val kristallMinenPlaceholder = KristallMinenPlaceholder()
val steinbruchPlaceholder = SteinbruchPlaceholder()

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

    placeholderManager.registerPlayerPlaceholder("%rankord%", 5000) { player ->
        (player.player as Player).toCringeUser().rank.ordinal
    }

    placeholderManager.registerPlayerPlaceholder("%title%", 2000) { player ->
        (player.player as Player).toCringeUser().title.display
    }

    placeholderManager.registerPlayerPlaceholder("%color%", 5000) { player ->
        (player.player as Player).toCringeUser().rank.color
    }

    placeholderManager.registerPlayerPlaceholder("%coins%", 1000) { player ->
        (player.player as Player).toCringeUser().coins
    }

    placeholderManager.registerPlayerPlaceholder("%idlecash%", 1000) { player ->
        (player.player as Player).toCringeUser().idleCash.display
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
    steinbruchPlaceholder.register()
}

fun unregisterPlaceholders() {
    cratePlaceholder.unregister()
    kristallMinenPlaceholder.unregister()
    steinbruchPlaceholder.unregister()
}