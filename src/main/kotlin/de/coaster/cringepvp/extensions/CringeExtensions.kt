package de.coaster.cringepvp.extensions

import de.coaster.cringepvp.database.createCringeUser
import de.coaster.cringepvp.database.getCringeUserOrNull
import de.coaster.cringepvp.database.model.CringeUser
import de.coaster.cringepvp.managers.PlayerCache
import de.coaster.cringepvp.utils.ItemStackConverter
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

fun ItemStack.setReceiver(player: Player): ItemStack {
    this.editMeta { meta -> meta.persistentDataContainer.set(NamespacedKey.minecraft("pickup-receiver"), PersistentDataType.STRING, player.uniqueId.toString()) }
    return this
}

fun ItemStack.removeReceiver(): ItemStack {
    this.editMeta { meta -> meta.persistentDataContainer.remove(NamespacedKey.minecraft("pickup-receiver")) }
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

fun Player.soundExecution() {
    playSound(location, Sound.ENTITY_ITEM_PICKUP, .75F, 2F)
    playSound(location, Sound.ITEM_ARMOR_EQUIP_LEATHER, .25F, 2F)
    playSound(location, Sound.ITEM_ARMOR_EQUIP_CHAIN, .1F, 2F)
}


var Player.isBuilder : Boolean
    get() = hasPermission("rainbowislands.builder") && scoreboardTags.contains("builder")
    set(value) {
        if (value) {
            if(hasPermission("rainbowislands.builder")) {
                scoreboardTags.add("builder")
            }
        } else {
            scoreboardTags.remove("builder")
        }
    }

fun Player.saveInventory(gameMode: GameMode) {
    persistentDataContainer.set(NamespacedKey.minecraft("inventory.${gameMode.name.lowercase()}.${uniqueId}"), PersistentDataType.STRING, ItemStackConverter.toBase64(inventory))
}

fun Player.loadInventory(gameMode: GameMode) {
    val inventoryString = persistentDataContainer.get(NamespacedKey.minecraft("inventory.${gameMode.name.lowercase()}.${uniqueId}"), PersistentDataType.STRING)
    if (inventoryString != null) {
        ItemStackConverter.fromBase64(inventoryString)?.let {
            it.contents.forEachIndexed { index, itemStack ->  inventory.setItem(index, itemStack) }
        }
    }
}