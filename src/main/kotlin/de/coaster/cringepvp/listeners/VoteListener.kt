package de.coaster.cringepvp.listeners

import com.vexsoftware.votifier.model.VotifierEvent
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.extensions.addTitle
import de.coaster.cringepvp.extensions.toCringeUser
import de.coaster.cringepvp.managers.PlayerCache
import dev.fruxz.stacked.text
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class VoteListener : Listener {

    @EventHandler
    fun onVote(event: VotifierEvent) = with(event) {
        println("Received vote from ${vote.serviceName} for ${vote.username}")
        val offlinePlayer = Bukkit.getOfflinePlayer(vote.username)
        val playerUUID = offlinePlayer.uniqueId

        var cringeUser = playerUUID.toCringeUser()
        cringeUser = cringeUser.copy(votes = cringeUser.votes + 1, voteKeys = cringeUser.voteKeys + 1)
        PlayerCache.updateCringeUser(cringeUser)

        Bukkit.getPlayer(playerUUID)?.let { player ->
            player.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Vote erhalten!</gray>"))
            player.sendMessage(text("<gray>Vielen Dank für deinen Vote auf</gray> <white>${vote.serviceName}"))
            player.sendMessage(text("<gray>Du hast nun</gray> <white>${cringeUser.votes}</white> <gray>Votes</gray> und <white>${cringeUser.voteKeys}</white> <gray>Vote Keys.</gray>"))
            player.sendMessage(text("<gray>Löse Votekeys am Spawn gegen tolle Items ein.</gray>"))
        }

        offlinePlayer.addTitle(Titles.FIRST_VOTE)
        if(cringeUser.votes >= 10) {
            offlinePlayer.addTitle(Titles.HEAVY_VOTER)
        }
    }

}