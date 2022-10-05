package de.coaster.cringepvp.extensions

import de.coaster.cringepvp.database.createCringeUser
import de.coaster.cringepvp.database.getCringeUserOrNull
import de.coaster.cringepvp.database.model.CringeUser
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.managers.PlayerCache
import de.coaster.cringepvp.utils.ItemStackConverter
import de.fruxz.ascend.tool.timing.calendar.Calendar
import de.fruxz.sparkle.framework.extension.effect.buildMelody
import de.fruxz.sparkle.framework.extension.effect.playEffect
import de.fruxz.sparkle.framework.extension.effect.soundOf
import de.fruxz.stacked.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*


fun Player.toCringeUser(): CringeUser {
    return PlayerCache.get(uniqueId)
}

fun UUID.toCringeUser(): CringeUser {
    return PlayerCache.get(this)
}

fun UUID.toCringeUserDB(): CringeUser {
    return getCringeUserOrNull(this) ?: createCringeUser(CringeUser(this, Bukkit.getPlayer(this)?.name ?: Bukkit.getOfflinePlayer(this).name ?: "Unknown"))
}

fun UUID.toOfflinePlayer(): OfflinePlayer {
    return Bukkit.getOfflinePlayer(this)
}

fun broadcastActionbar(component: Component) {
    Bukkit.getOnlinePlayers().forEach { it.sendActionBar(component) }
}

fun Location.toCringeString(): String {
    return "${world.name}_${blockX}_${blockY}_${blockZ}"
}

fun String.toCringeLocation(): Location {
    val split = this.split("_")
    return Location(Bukkit.getWorld(split[0]), split[1].toDouble(), split[2].toDouble(), split[3].toDouble())
}

fun convertAreaToString(area: Pair<Location, Location>): String {
    return "${area.first.toCringeString()}+${area.second.toCringeString()}"
}

fun convertStringToArea(string: String): Pair<Location, Location> {
    val split = string.split("+")
    return Pair(split[0].toCringeLocation(), split[1].toCringeLocation())
}

fun ItemStack.setReceiver(player: Player): ItemStack {
    this.editMeta { meta -> meta.persistentDataContainer.set(NamespacedKey.minecraft("pickup-receiver"), PersistentDataType.STRING, player.uniqueId.toString()) }
    return this
}

fun ItemStack.removeReceiver(): ItemStack {
    this.editMeta { meta -> meta.persistentDataContainer.remove(NamespacedKey.minecraft("pickup-receiver")) }
    return this
}

var ItemStack.soulbound: Boolean
    get() = this.hasItemMeta() && this.itemMeta.hasLore() && this.itemMeta.lore()?.map { it.plainText }?.find { s: String -> s.contains("Soulbound") } != null
    set(value) {
        if (value) {
            if(!this.soulbound) {
                this.editMeta { meta ->
                    val lore = meta.lore() ?: mutableListOf()
                    lore.add(text(" "))
                    lore.add(text("<color:#4aabff><b>Soulbound</b></color> <dark_gray>×</dark_gray> <gray>Du behälst dieses Item beim Tod</gray>"))
                    meta.lore(lore)
                }
            }
        } else {
            this.editMeta { meta ->
                val lore = meta.lore() ?: mutableListOf()
                lore.removeIf { it.plainText.contains("Soulbound") }
                meta.lore(lore)
            }
        }
    }

fun ItemStack.sign(cringeUser: CringeUser): ItemStack {
    this.editMeta { meta ->
        val lore = meta.lore() ?: mutableListOf()
        lore.add(text(" "))
        lore.add(text("<color:#34ace0>Dieses Item wurde von <color:${cringeUser.rank.color}>${cringeUser.uuid.toOfflinePlayer().name}</color> signiert</color>"))
        lore.add(text("<gray>Am ${Calendar.now().getFormatted(Locale.GERMANY)}"))
        meta.lore(lore)
    }
    return this
}

fun ItemStack.getReceiver(): UUID? {
    return this.itemMeta?.persistentDataContainer?.get(NamespacedKey.minecraft("pickup-receiver"), PersistentDataType.STRING)?.let { UUID.fromString(it) }
}

val Component.plainText: String
    get() = PlainTextComponentSerializer.plainText().serialize(this).replace("[", "").replace("]", "")

fun Inventory.setItems(slotRange: IntRange, item: ItemStack) {
    slotRange.forEach { this.setItem(it, item) }
}

val executionSoundMelody = buildMelody {
    beat {
        sound(soundOf(Sound.ENTITY_ITEM_PICKUP, .75, 2))
        sound(soundOf(Sound.ITEM_ARMOR_EQUIP_LEATHER, .25, 2))
        sound(soundOf(Sound.ITEM_ARMOR_EQUIP_CHAIN, .1, 2))
    }
}

fun Player.soundExecution() = this.playEffect(executionSoundMelody) // This makes the player the sound source

val failSoundMelody = buildMelody {
    beat {
        sound(soundOf(Sound.ENTITY_ITEM_PICKUP, volume = .75, pitch = 2))
        sound(soundOf(Sound.ENTITY_GENERIC_EXPLODE, volume = .25, pitch = 2))
        sound(soundOf(Sound.ENTITY_GHAST_SCREAM, volume = .1, pitch = 2))
    }
}

fun Player.failSoundExecution() = this.playEffect(failSoundMelody) // This makes the player the sound source


var Player.isBuilder : Boolean
    get() = hasPermission("cringepvp.builder") && scoreboardTags.contains("builder")
    set(value) {
        if (value) {
            if(hasPermission("cringepvp.builder")) {
                scoreboardTags.add("builder")
            }
        } else {
            scoreboardTags.remove("builder")
        }
    }

fun Player.saveInventory(invToSave: Inventory, key: String) {
    persistentDataContainer.set(NamespacedKey.minecraft("inventory.${key}"), PersistentDataType.STRING, ItemStackConverter.toBase64(invToSave))
}

fun Player.removeInventory(key: String) {
    persistentDataContainer.remove(NamespacedKey.minecraft("inventory.${key}"))
}

fun Player.saveInventory(gameMode: GameMode) {
    saveInventory(inventory, gameMode.name.lowercase())
}

fun Player.loadInventory(key: String) {
    val inventoryString = persistentDataContainer.get(NamespacedKey.minecraft("inventory.${key}"), PersistentDataType.STRING)
    if (inventoryString != null) {
        ItemStackConverter.fromBase64(inventoryString)?.let {
            it.contents.forEachIndexed { index, itemStack ->  inventory.setItem(index, itemStack) }
        }
    }
}

fun Player.loadInventory(gameMode: GameMode) {
    loadInventory(gameMode.name.lowercase())
}

fun CringeUser.hasTitle(title: Titles): Boolean {
    return ownedTitles.contains(title)
}

fun OfflinePlayer.addTitle(title: Titles) {
    var targetCringeUser = this.uniqueId.toCringeUser()
    if(targetCringeUser.hasTitle(title)) return
    targetCringeUser = targetCringeUser.copy(ownedTitles = targetCringeUser.ownedTitles + title)
    PlayerCache.updateCringeUser(targetCringeUser)
    if(!this.isOnline) {
        PlayerCache.remove(this.uniqueId)
    } else {
        this.player?.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du hast den Titel <gold>${title.display}</gold> erhalten. Rüste ihn jetzt in <yellow>/menu</yellow> aus.</gray>"))
    }
}

fun Player.addTitle(title: Titles) {
    var targetCringeUser = this.uniqueId.toCringeUser()
    if(targetCringeUser.hasTitle(title)) return
    targetCringeUser = targetCringeUser.copy(ownedTitles = targetCringeUser.ownedTitles + title)
    PlayerCache.updateCringeUser(targetCringeUser)
    sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du hast den Titel <gold>${title.display}</gold> erhalten. Rüste ihn jetzt in <yellow>/menu</yellow> aus.</gray>"))
}