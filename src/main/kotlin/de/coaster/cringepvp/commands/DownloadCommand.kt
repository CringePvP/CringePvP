package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.moltenKt.unfold.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@RegisterCommand(
    name = "download",
    permission = "",
)
class DownloadCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return true
        sender.sendMessage("§aDownloading...")


        sender.setResourcePack("https://cdn.devsky.one/zips/MineBricks.zip", "79b6fa715d53ab97b83f642b2902ed37ff4b4d0a", true, text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>ResourcePack</gray>\n" +
                "<i><gray>Du hast unser geheimes Pack gefunden.\n" +
                "Aktiviere es jetzt!</gray></i>"))
        return true
    }

}