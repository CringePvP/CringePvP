package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.moltenKt.paper.extension.display.ui.itemStack
import de.moltenKt.unfold.text
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@RegisterCommand(
    name = "menu",
    description = "Öffne das Menü",
    permission = "",
    aliases = ["m", "menü"]
)
class MenuCommand : CommandExecutor {

    val inventory = Bukkit.createInventory(null, 27, text("<gold>Dein Menü</gold>")).apply {
        this.setItem(11, Material.NAME_TAG.itemStack { editMeta { meta -> meta.displayName(text("<gold>Deine Title</gold>")) } })
        this.setItem(15, Material.DIAMOND_SWORD.itemStack { editMeta { meta -> meta.displayName(text("<gold>Deine Skills</gold>")) } })
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true

        // Inhalte
        // Title, Skills upgraden,
        sender.openInventory(inventory)

        return true
    }
}