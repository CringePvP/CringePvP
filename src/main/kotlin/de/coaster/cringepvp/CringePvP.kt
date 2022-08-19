package de.coaster.cringepvp

import de.coaster.cringepvp.database.DatabaseManager
import de.coaster.cringepvp.database.TableUserTitles
import de.coaster.cringepvp.database.TableUsers
import de.coaster.cringepvp.database.smartTransaction
import de.coaster.cringepvp.enums.Keys
import de.coaster.cringepvp.enums.Rarity
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.managers.CoroutineManager
import de.coaster.cringepvp.managers.ItemManager
import de.coaster.cringepvp.managers.PlayerCache
import de.coaster.cringepvp.managers.RegisterManager.registerAll
import de.coaster.cringepvp.placeholders.registerPlaceholders
import de.coaster.cringepvp.placeholders.unregisterPlaceholders
import de.moltenKt.core.extension.empty
import de.moltenKt.core.tool.smart.identification.Identity
import de.moltenKt.paper.extension.display.ui.itemStack
import de.moltenKt.paper.extension.tasky.delayed
import de.moltenKt.paper.extension.tasky.doSync
import de.moltenKt.paper.structure.app.App
import de.moltenKt.paper.structure.app.AppCache
import de.moltenKt.paper.structure.app.AppCompanion
import de.moltenKt.paper.structure.app.cache.CacheDepthLevel
import de.moltenKt.unfold.text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.SchemaUtils
import java.util.*
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.seconds

class CringePvP : JavaPlugin() {

    companion object {
        lateinit var instance: CringePvP
            private set

        lateinit var coroutineScope: CoroutineScope
            private set
    }

    init {
        instance = this
        coroutineScope = CoroutineScope(Dispatchers.Default)
    }

    override fun onEnable() {
        // Plugin startup logic

        DatabaseManager.database
        smartTransaction {
            SchemaUtils.create(TableUsers, TableUserTitles)
        }

        val time = measureTimeMillis {
            registerAll()
            registerPlaceholders()
        }
        println("Plugin enabled in $time ms")

        println("CringePvP is now tweaking your SkyPvP behavior!")
        ItemManager
        CoroutineManager


        val lootItems = mapOf(
            Material.POISONOUS_POTATO to Rarity.NORMAL,
            Material.ROTTEN_FLESH to Rarity.NORMAL,
            Material.DRIED_KELP to Rarity.NORMAL,
            Material.SPIDER_EYE to Rarity.NORMAL,

            Material.ARROW to Rarity.NORMAL,

            Material.CARROT to Rarity.NORMAL,
            Material.POTATO to Rarity.NORMAL,
            Material.BEETROOT to Rarity.NORMAL,
            Material.BREAD to Rarity.NORMAL,
            Material.MELON_SLICE to Rarity.NORMAL,
            Material.BEETROOT_SOUP to Rarity.VIP,
            Material.MUSHROOM_STEW to Rarity.VIP,
            Material.APPLE to Rarity.VIP,
            Material.MUTTON to Rarity.PREMIUM,
            Material.BEEF to Rarity.PREMIUM,
            Material.COOKED_BEEF to Rarity.ULTIMATE,
            Material.COOKED_CHICKEN to Rarity.ULTIMATE,
            Material.COOKED_COD to Rarity.ULTIMATE,
            Material.COOKED_MUTTON to Rarity.ULTIMATE,
            Material.COOKED_PORKCHOP to Rarity.ULTIMATE,
            Material.COOKED_RABBIT to Rarity.ULTIMATE,
            Material.COOKED_SALMON to Rarity.ULTIMATE,
            Material.PUMPKIN_PIE to Rarity.EPIC,
            Material.HONEY_BOTTLE to Rarity.EPIC,

            Material.SPECTRAL_ARROW to Rarity.LEGENDARY,
            Material.TIPPED_ARROW to Rarity.MYTHICAL,
            Material.GOLDEN_APPLE to Rarity.ANCIENT,
            Material.CHARCOAL to Rarity.DIVINE,

            Material.ENCHANTED_GOLDEN_APPLE to Rarity.IMMORTAL,
        )

        var itemList = mapOf<Rarity, List<ItemStack>>()
        var total = 0
        lootItems.forEach { (material, rarity) ->
            val item = material.itemStack { editMeta { meta -> meta.displayName(text("<red><b><translate:${material.translationKey()}></b></red>")) } }.asQuantity(16)
            itemList += if(itemList[rarity] == null) {
                rarity to listOf(item)
            } else {
                rarity to itemList[rarity]!!.plus(item)
            }
            total++
        }
        println("Loaded $total items")

        Rarity.values().filter { it != Rarity.NORMAL && it != Rarity.VOTE && it != Rarity.EXCLUSIVE }.forEach { rarity ->
            val rankItem = Material.FLOWER_BANNER_PATTERN.itemStack { editMeta { meta -> meta.displayName(text("<color:${rarity.color}><b>Rank</b></color> <dark_gray>×</dark_gray> <gray>${rarity.name}</gray>")) } }
            val keyItem = Material.NAME_TAG.itemStack { editMeta { meta -> meta.displayName(text("<yellow><b>Key</b></yellow> <dark_gray>×</dark_gray> <gray>${rarity.name}</gray>")) } }

            itemList += if(itemList[rarity] == null) {
                rarity to listOf(rankItem, keyItem)
            } else {
                rarity to itemList[rarity]!!.plus(rankItem).plus(keyItem)
            }
        }

        val titleMap = mapOf(
            Titles.MystischerMagier to Rarity.MYTHICAL
        )

        val randomItemsMap = mapOf(
            Pair(Material.LEATHER_HELMET, 1) to Rarity.NORMAL,
            Pair(Material.LEATHER_CHESTPLATE, 1) to Rarity.NORMAL,
            Pair(Material.LEATHER_LEGGINGS, 1) to Rarity.NORMAL,
            Pair(Material.LEATHER_BOOTS, 1) to Rarity.NORMAL,
            Pair(Material.WOODEN_SWORD, 1) to Rarity.NORMAL,
            Pair(Material.WOODEN_AXE, 1) to Rarity.NORMAL,
            Pair(Material.WOODEN_HOE, 1) to Rarity.NORMAL,
            Pair(Material.WOODEN_PICKAXE, 1) to Rarity.NORMAL,
            Pair(Material.WOODEN_SHOVEL, 1) to Rarity.NORMAL,
            Pair(Material.GOLD_NUGGET, 1) to Rarity.NORMAL,
            Pair(Material.GOLD_NUGGET, 2) to Rarity.NORMAL,
            Pair(Material.GOLD_NUGGET, 3) to Rarity.NORMAL,

            Pair(Material.GOLD_NUGGET, 4) to Rarity.VIP,
            Pair(Material.GOLD_NUGGET, 5) to Rarity.VIP,

            Pair(Material.CHAINMAIL_HELMET, 1) to Rarity.PREMIUM,
            Pair(Material.CHAINMAIL_CHESTPLATE, 1) to Rarity.PREMIUM,
            Pair(Material.CHAINMAIL_LEGGINGS, 1) to Rarity.PREMIUM,
            Pair(Material.CHAINMAIL_BOOTS, 1) to Rarity.PREMIUM,
            Pair(Material.STONE_SWORD, 1) to Rarity.PREMIUM,
            Pair(Material.STONE_AXE, 1) to Rarity.PREMIUM,
            Pair(Material.STONE_HOE, 1) to Rarity.PREMIUM,
            Pair(Material.STONE_PICKAXE, 1) to Rarity.PREMIUM,
            Pair(Material.STONE_SHOVEL, 1) to Rarity.PREMIUM,
            Pair(Material.GOLD_NUGGET, 8) to Rarity.PREMIUM,
            Pair(Material.GOLD_NUGGET, 10) to Rarity.PREMIUM,

            Pair(Material.GOLD_NUGGET, 12) to Rarity.ULTIMATE,
            Pair(Material.GOLD_NUGGET, 15) to Rarity.ULTIMATE,

            Pair(Material.GOLDEN_HELMET, 1) to Rarity.EPIC,
            Pair(Material.GOLDEN_CHESTPLATE, 1) to Rarity.EPIC,
            Pair(Material.GOLDEN_LEGGINGS, 1) to Rarity.EPIC,
            Pair(Material.GOLDEN_BOOTS, 1) to Rarity.EPIC,
            Pair(Material.GOLDEN_SWORD, 1) to Rarity.EPIC,
            Pair(Material.GOLDEN_AXE, 1) to Rarity.EPIC,
            Pair(Material.GOLDEN_HOE, 1) to Rarity.EPIC,
            Pair(Material.GOLDEN_PICKAXE, 1) to Rarity.EPIC,
            Pair(Material.GOLDEN_SHOVEL, 1) to Rarity.EPIC,
            Pair(Material.GOLD_NUGGET, 18) to Rarity.EPIC,
            Pair(Material.GOLD_NUGGET, 20) to Rarity.EPIC,

            Pair(Material.IRON_HELMET, 1) to Rarity.LEGENDARY,
            Pair(Material.IRON_CHESTPLATE, 1) to Rarity.LEGENDARY,
            Pair(Material.IRON_LEGGINGS, 1) to Rarity.LEGENDARY,
            Pair(Material.IRON_BOOTS, 1) to Rarity.LEGENDARY,
            Pair(Material.IRON_SWORD, 1) to Rarity.LEGENDARY,
            Pair(Material.IRON_AXE, 1) to Rarity.LEGENDARY,
            Pair(Material.IRON_HOE, 1) to Rarity.LEGENDARY,
            Pair(Material.IRON_PICKAXE, 1) to Rarity.LEGENDARY,
            Pair(Material.IRON_SHOVEL, 1) to Rarity.LEGENDARY,
            Pair(Material.GOLD_NUGGET, 24) to Rarity.LEGENDARY,
            Pair(Material.GOLD_NUGGET, 26) to Rarity.LEGENDARY,

            Pair(Material.BEACON, 1) to Rarity.MYTHICAL,
            Pair(Material.GOLD_NUGGET, 28) to Rarity.MYTHICAL,
            Pair(Material.GOLD_NUGGET, 32) to Rarity.MYTHICAL,
            Pair(Material.EMERALD, 1) to Rarity.MYTHICAL,

            Pair(Material.DIAMOND_HELMET, 1) to Rarity.ANCIENT,
            Pair(Material.DIAMOND_CHESTPLATE, 1) to Rarity.ANCIENT,
            Pair(Material.DIAMOND_LEGGINGS, 1) to Rarity.ANCIENT,
            Pair(Material.DIAMOND_BOOTS, 1) to Rarity.ANCIENT,
            Pair(Material.DIAMOND_SWORD, 1) to Rarity.ANCIENT,
            Pair(Material.DIAMOND_AXE, 1) to Rarity.ANCIENT,
            Pair(Material.DIAMOND_HOE, 1) to Rarity.ANCIENT,
            Pair(Material.DIAMOND_PICKAXE, 1) to Rarity.ANCIENT,
            Pair(Material.DIAMOND_SHOVEL, 1) to Rarity.ANCIENT,
            Pair(Material.GOLD_NUGGET, 35) to Rarity.DIVINE,
            Pair(Material.GOLD_NUGGET, 38) to Rarity.DIVINE,
            Pair(Material.EMERALD, 2) to Rarity.DIVINE,
            Pair(Material.NAUTILUS_SHELL, 1) to Rarity.DIVINE,

            Pair(Material.GOLD_NUGGET, 40) to Rarity.DIVINE,
            Pair(Material.GOLD_NUGGET, 42) to Rarity.DIVINE,
            Pair(Material.EMERALD, 3) to Rarity.DIVINE,
            Pair(Material.NAUTILUS_SHELL, 2) to Rarity.DIVINE,

            Pair(Material.NETHERITE_HELMET, 1) to Rarity.IMMORTAL,
            Pair(Material.NETHERITE_CHESTPLATE, 1) to Rarity.IMMORTAL,
            Pair(Material.NETHERITE_LEGGINGS, 1) to Rarity.IMMORTAL,
            Pair(Material.NETHERITE_BOOTS, 1) to Rarity.IMMORTAL,
            Pair(Material.NETHERITE_SWORD, 1) to Rarity.IMMORTAL,
            Pair(Material.NETHERITE_AXE, 1) to Rarity.IMMORTAL,
            Pair(Material.NETHERITE_HOE, 1) to Rarity.IMMORTAL,
            Pair(Material.NETHERITE_PICKAXE, 1) to Rarity.IMMORTAL,
            Pair(Material.NETHERITE_SHOVEL, 1) to Rarity.IMMORTAL,
            Pair(Material.GOLD_NUGGET, 54) to Rarity.IMMORTAL,
            Pair(Material.GOLD_NUGGET, 64) to Rarity.IMMORTAL,
            Pair(Material.EMERALD, 5) to Rarity.IMMORTAL,
            Pair(Material.NAUTILUS_SHELL, 3) to Rarity.IMMORTAL,
        )

        randomItemsMap.forEach { (itemPair, rarity) ->
            val item = itemPair.first.itemStack { editMeta { meta ->
                when(itemPair.first) {
                    Material.GOLD_NUGGET -> meta.displayName(text("<color:#ffb142><b>Coin</b>"))
                    Material.EMERALD -> meta.displayName(text("<color:#26de81><b>Gem</b>"))
                    Material.AMETHYST_SHARD -> meta.displayName(text("<color:#4aabff><b>Kristall</b>"))
                    Material.NAUTILUS_SHELL -> meta.displayName(text("<color:#b33939><b>Relikt</b>"))
                    else -> meta.displayName(text("<red><b><translate:${itemPair.first.translationKey()}></b></red>"))
                }
            } }.asQuantity(itemPair.second)
            itemList += if(itemList[rarity] == null) {
                rarity to listOf(item)
            } else {
                rarity to itemList[rarity]!!.plus(item)
            }
        }

        titleMap.forEach { (title, rarity) ->
            val titleItem = Material.PAPER.itemStack { editMeta { meta -> meta.displayName(text("<color:${rarity.color}><b>${title.name}</b></color>")) } }
            itemList += if(itemList[rarity] == null) {
                rarity to listOf(titleItem)
            } else {
                rarity to itemList[rarity]!!.plus(titleItem)
            }
        }

        itemList.forEach { (rarity, items) ->
            println("$rarity: ${items.size}")
            ItemManager.addItems(rarity, items)
        }
        println("Loaded ${itemList.size} rarity types an a total of ${itemList.values.sumOf { it.size }} items")
    }

    override fun onDisable() {
        unregisterPlaceholders()
        PlayerCache.saveAll()
        coroutineScope.coroutineContext.cancelChildren()
    }
}