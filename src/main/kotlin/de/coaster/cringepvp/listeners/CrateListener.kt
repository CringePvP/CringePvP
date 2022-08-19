package de.coaster.cringepvp.listeners

import com.destroystokyo.paper.ParticleBuilder
import de.coaster.cringepvp.enums.Keys
import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.enums.Rarity
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.extensions.*
import de.coaster.cringepvp.managers.CoroutineManager
import de.coaster.cringepvp.managers.ItemManager
import de.coaster.cringepvp.managers.PlayerCache
import de.coaster.cringepvp.utils.FileConfig
import de.moltenKt.core.extension.data.randomInt
import de.moltenKt.paper.extension.display.ui.itemStack
import de.moltenKt.unfold.text
import eu.decentsoftware.holograms.api.DHAPI
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.data.type.Chest
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.lang.Integer.max
import java.util.*
import kotlin.math.min
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


class CrateListener : Listener {

    @EventHandler
    fun onClickOnCreate(event: PlayerInteractEvent) = with(event) {
        if (clickedBlock == null) return@with
        if (action != Action.RIGHT_CLICK_BLOCK) return@with
        if (hand != EquipmentSlot.HAND) return@with

        if (clickedBlock?.blockData is Chest) return@with onLootChestClick(this)
        if (clickedBlock?.type == Material.PLAYER_HEAD) return@with onCrateClick(this)
    }

    private fun onCrateClick(event: PlayerInteractEvent) = with(event) {
        val location = clickedBlock!!.location.toCringeString()
        val config = FileConfig("crate.yml")

        if (!config.contains(location)) {
            config.set(location, "NIX")
            config.saveConfig()
        }

        val type = config.getString(location)
        if (type == "NIX") return@with

        val rarity = Rarity.values().find { it.name == type } ?: return@with
        val key = Keys.values().find { it.name == type } ?: return@with
        val cringeUser = player.toCringeUser()

        Bukkit.createInventory(
            null,
            9,
            text("<${rarity.color}>${rarity.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }} Crate")
        )
            .apply {
                setItems(0..8, Material.GRAY_STAINED_GLASS_PANE.itemStack {
                    editMeta { meta -> meta.displayName(text(" ")) }
                })
                setItem(
                    3,
                    Material.CHEST.itemStack { editMeta { meta -> meta.displayName(text("<#2ecc71>Crate öffnen")) } })
                setItem(
                    5,
                    Material.NAME_TAG.itemStack {
                        editMeta { meta ->
                            meta.displayName(
                                text(
                                    "<#e74c3c>Keys: ${
                                        key.playerReference.get(cringeUser)
                                    }"
                                )
                            )
                        }
                    }.asQuantity(
                        min(64, max(1, key.playerReference.get(cringeUser).toInt()))
                    )
                )
            }.let {
                player.openInventory(it)
            }

        return@with
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) = with(event) {
        val titleComponent = view.title()
        val title = titleComponent.plainText
        if (!title.endsWith("Crate")) return@with
        val key = Keys.values().find { it.name == title.replace("Crate", "").trim() } ?: return@with
        isCancelled = true
        if (currentItem == null) return@with
        if (currentItem!!.type != Material.CHEST) return@with
        val player = whoClicked as Player
        player.closeInventory()

        if (player.isInCooldown("crateOpen")) {
            player.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Bitte warte noch ${player.getCooldown("crateOpen")}, bis du eine neue Kiste öffnest.</gray>"))
            return@with
        }

        var cringeUser = player.toCringeUser()
        if (key.playerReference.get(cringeUser) < 1) {
            player.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du hast keine Keys mehr!</gray>"))
            return@with
        }
        cringeUser = cringeUser.copy().apply {
            key.playerReference.set(this, key.playerReference.get(this) - 1)
        }
        PlayerCache.updateCringeUser(cringeUser)

        val config = FileConfig("crate.yml")
        // Reverse search for crate location
        val crateLocation =
            config.getKeys(true).find { config.getString(it) == key.name }?.toCringeLocation() ?: return@with
        val minRarity = Rarity.values().find { it.name == key.name } ?: return@with
        val rarity = minRarity.getAllBelow()
        player.setCooldown("crateOpen", key.dropAmount.seconds)
        CoroutineManager.shootItems(
            crateLocation.add(0.5, 1.2, 0.5),
            player,
            ItemManager.getRandomItemsWithOneMinRarity(key.dropAmount, rarity, minRarity).map { it.setReceiver(player) }.toTypedArray()
        )
    }

    private fun onLootChestClick(event: PlayerInteractEvent) = with(event) {
        isCancelled = true

        val location = clickedBlock!!.location

        if (location.isInCooldown()) {
            player.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Bitte warte noch ${location.getCooldown()}</gray>"))
            return@with
        }
        location.setCooldown(3.minutes)

        var hologram = DHAPI.getHologram(location.toCringeString())
        if (hologram == null) {
            hologram = DHAPI.createHologram(
                location.toCringeString(), location.toCenterLocation().add(0.0, 1.2, 0.0), listOf(
                    "#7ed6df・ Cooldown ・",
                    "#badc58%cooldown_${location.toCringeString()}%"
                )
            )
            hologram.isAlwaysFacePlayer = true
            hologram.updateRange = 20
            hologram.updateInterval = 1
        }

        CoroutineManager.shootItems(clickedBlock!!.location.add(0.5, 1.2, 0.5), player, ItemManager.getItems(3))
        val chest = clickedBlock?.state as org.bukkit.block.Chest
        chest.open()
        CoroutineManager.startCoroutine({ chest.close() }, 3500)
    }

    @EventHandler
    fun onPickupItem(event: EntityPickupItemEvent) = with(event) {
        if (entity !is Player) return@with

        val receiver = item.itemStack.getReceiver()
        if (receiver != null) {
            if (entity.uniqueId != receiver) {
                isCancelled = true
                return@with
            }
        }

        when (item.itemStack.type) {
            Material.GOLD_NUGGET -> {
                val itemStack = item.itemStack
                val displayName = itemStack.displayName().plainText
                if (displayName.startsWith("Coin")) {
                    val amount = itemStack.amount

                    var cringeUser = (entity as Player).toCringeUser()
                    cringeUser = cringeUser.copy(coins = cringeUser.coins + amount)
                    PlayerCache.updateCringeUser(cringeUser)

                    isCancelled = true
                    pickUpItem()

                    val rarityName = getPlainRarityName(itemStack)
                    val rarity = Rarity.values().find { it.name.equals(rarityName, true) } ?: Rarity.NORMAL
                    entity.sendActionBar(text("<${rarity.color}>${rarity.name} <dark_gray>»</dark_gray> <color:#ffb142><b>Coins</b></color> <dark_gray>×</dark_gray> <gray>$amount</gray>"))
                }
            }

            Material.EMERALD -> {
                val itemStack = item.itemStack
                val displayName = itemStack.displayName().plainText
                if (displayName.startsWith("Gem")) {
                    val amount = itemStack.amount

                    var cringeUser = (entity as Player).toCringeUser()
                    cringeUser = cringeUser.copy(gems = cringeUser.gems + amount)
                    PlayerCache.updateCringeUser(cringeUser)

                    isCancelled = true
                    pickUpItem()

                    val rarityName = getPlainRarityName(itemStack)
                    val rarity = Rarity.values().find { it.name.equals(rarityName, true) } ?: Rarity.NORMAL
                    entity.sendActionBar(text("<${rarity.color}>${rarity.name} <dark_gray>»</dark_gray> <color:#26de81><b>Gems</b></color> <dark_gray>×</dark_gray> <gray>$amount</gray>"))
                }
            }

            Material.AMETHYST_SHARD -> {
                val itemStack = item.itemStack
                val displayName = itemStack.displayName().plainText
                if (displayName.startsWith("Relikt")) {
                    val amount = itemStack.amount

                    var cringeUser = (entity as Player).toCringeUser()
                    cringeUser = cringeUser.copy(gems = cringeUser.crystals + amount)
                    PlayerCache.updateCringeUser(cringeUser)

                    isCancelled = true
                    pickUpItem()

                    val rarityName = getPlainRarityName(itemStack)
                    val rarity = Rarity.values().find { it.name.equals(rarityName, true) } ?: Rarity.NORMAL
                    entity.sendActionBar(text("<${rarity.color}>${rarity.name} <dark_gray>»</dark_gray> <color:#4aabff><b>Kristall</b></color> <dark_gray>×</dark_gray> <gray>$amount</gray>"))
                }
            }

            Material.NAUTILUS_SHELL -> {
                val itemStack = item.itemStack
                val displayName = itemStack.displayName().plainText
                if (displayName.startsWith("Relikt")) {
                    val amount = itemStack.amount

                    var cringeUser = (entity as Player).toCringeUser()
                    cringeUser = cringeUser.copy(gems = cringeUser.relicts + amount)
                    PlayerCache.updateCringeUser(cringeUser)

                    isCancelled = true
                    pickUpItem()

                    val rarityName = getPlainRarityName(itemStack)
                    val rarity = Rarity.values().find { it.name.equals(rarityName, true) } ?: Rarity.NORMAL
                    entity.sendActionBar(text("<${rarity.color}>${rarity.name} <dark_gray>»</dark_gray> <color:#b33939><b>Relikt</b></color> <dark_gray>×</dark_gray> <gray>$amount</gray>"))
                }
            }

            Material.FLOWER_BANNER_PATTERN -> {
                val itemStack = item.itemStack
                val displayName = itemStack.displayName().plainText
                if (displayName.startsWith("Rank")) {
                    val rang = displayName.split("×")[1].trim()
                    val rank = Ranks.values().find { it.name.equals(rang, true) } ?: Ranks.Spieler
                    var cringeUser = (entity as Player).toCringeUser()
                    val currentRank = Ranks.values().find { it.name == cringeUser.rank.name } ?: Ranks.Spieler

                    if (currentRank.ordinal <= rank.ordinal) {
                        itemStack.removeReceiver()
                        entity.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du hast bereits einen höheren Rang. Verschenke ihn doch!</gray>"))
                        return@with
                    }

                    cringeUser = cringeUser.copy(rank = rank)
                    PlayerCache.updateCringeUser(cringeUser)

                    isCancelled = true
                    pickUpItem()

                    val rarityName = getPlainRarityName(itemStack)
                    val rarity = Rarity.values().find { it.name.equals(rarityName, true) } ?: Rarity.NORMAL
                    entity.sendActionBar(text("<${rarity.color}>${rarity.name} <dark_gray>»</dark_gray> <color:${rank.color}><b>Rank</b></color> <dark_gray>×</dark_gray> <gray>${rank.name}</gray>"))
                }
            }

            Material.PAPER -> {
                val itemStack = item.itemStack
                val displayName = itemStack.displayName().plainText
                if (displayName.startsWith("Title")) {
                    val titleString = displayName.split("×")[1].trim()
                    val title = Titles.values().find { it.display.equals(titleString, true) || it.name.equals(titleString, true) } ?: Titles.NoTITLE
                    var cringeUser = (entity as Player).toCringeUser()

                    if (cringeUser.ownedTitles.contains(title)) {
                        itemStack.removeReceiver()
                        entity.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du hast bereits diesen Titel. Verschenke ihn doch!</gray>"))
                        return@with
                    }

                    cringeUser = cringeUser.copy(ownedTitles = cringeUser.ownedTitles + title)
                    PlayerCache.updateCringeUser(cringeUser)

                    isCancelled = true
                    pickUpItem()

                    val rarityName = getPlainRarityName(itemStack)
                    val rarity = Rarity.values().find { it.name.equals(rarityName, true) } ?: Rarity.NORMAL
                    entity.sendActionBar(text("<${rarity.color}>${rarity.name} <dark_gray>»</dark_gray> <blue><b>Title</b></blue> <dark_gray>×</dark_gray> <gray>${title.display}</gray>"))
                }
            }

            Material.NAME_TAG -> {
                val itemStack = item.itemStack
                val displayName = itemStack.displayName().plainText
                if (displayName.startsWith("Key")) {
                    val keyString = displayName.split("×")[1].trim()
                    val key = Keys.values().find { it.name.equals(keyString, true) } ?: Keys.NORMAL

                    var cringeUser = (entity as Player).toCringeUser()
                    cringeUser = cringeUser.copy().apply {
                        key.playerReference.set(this, key.playerReference.get(this) + itemStack.amount)
                    }
                    PlayerCache.updateCringeUser(cringeUser)

                    isCancelled = true
                    pickUpItem()

                    val rarityName = getPlainRarityName(itemStack)
                    val rarity = Rarity.values().find { it.name.equals(rarityName, true) } ?: Rarity.NORMAL
                    entity.sendActionBar(text("<${rarity.color}>${rarity.name} <dark_gray>»</dark_gray> <yellow><b>Key</b></yellow> <dark_gray>×</dark_gray> <gray>${key.name}</gray>"))
                }
            }
            else -> {}
        }

        item.itemStack.removeReceiver()
    }

    private fun getPlainRarityName(itemStack: ItemStack) = PlainTextComponentSerializer.plainText()
        .serialize(itemStack.lore()?.first() ?: text(": NORMAL"))
        .replace("[", "")
        .replace("]", "")
        .split(":")[1].trim()

    private fun EntityPickupItemEvent.pickUpItem() {
        val location = item.location.clone()
        item.remove()

        location.world.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1.5f)
        ParticleBuilder(Particle.BUBBLE_POP)
            .location(location)
            .count(randomInt(3..5))
            .offset(.35, .5, .35)
            .extra(.01)
            .spawn()
    }
}