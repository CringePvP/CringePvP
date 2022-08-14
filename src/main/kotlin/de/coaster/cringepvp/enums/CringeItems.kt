package de.coaster.cringepvp.enums

import de.coaster.cringepvp.extensions.toCringeUser
import de.moltenKt.paper.extension.display.ui.itemStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import de.moltenKt.unfold.text
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent


enum class CringeItems(val item: ItemStack, val onSpawn: ItemStack.() -> Unit = {}, val pickupAble: Boolean = true, val onPickUp: EntityPickupItemEvent.() -> Unit = {}) {
    GOLD(Material.GOLD_NUGGET.itemStack.asOne(), { itemMeta = itemMeta.clone().apply { displayName(text("<gold>Gold: ${de.moltenKt.core.extension.data.randomInt(2..25)}</gold>")) } },false,
        onPickUp@{
            if(entity !is Player) {
                isCancelled = true
                return@onPickUp
            }
            val player = entity as Player
            val gold = PlainTextComponentSerializer.plainText().serialize(item.customName() ?: text("Gold: 0")).replace("Gold: ", "").toIntOrNull() ?: 0
            var cringeUser = player.toCringeUser()
            cringeUser = cringeUser.copy(coins = cringeUser.coins + gold)
            de.coaster.cringepvp.managers.PlayerCache.updateCringeUser(cringeUser)
        });

    fun asItem(): ItemStack = item.apply { onSpawn() }
}

enum class Rarity(val rarity: Int) {
    NORMAL(1),
    VIP(2),
    PREMIUM(3),
    ULTIMATE(4),
    EPIC(5),
    LEGENDARY(6),
    MYTHICAL(7),
    ANCIENT(8),
    DIVINE(9),
    IMMORTAL(10);

    fun getAllBelow(highest: Rarity): List<Rarity> {
        return values().filter { it.rarity <= highest.rarity }
    }
}