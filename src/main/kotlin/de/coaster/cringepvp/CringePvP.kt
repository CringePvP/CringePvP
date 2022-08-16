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
            Material.BEACON to Rarity.PREMIUM,
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
            for(size in 1 .. material.maxStackSize) {
                val item = material.itemStack { editMeta { meta -> meta.displayName(text("<red><b><translate:${material.translationKey()}></b></red> <dark_gray>×</dark_gray> <gray>$size</gray>")) } }.asQuantity(size)
                itemList += if(itemList[rarity] == null) {
                    rarity to listOf(item)
                } else {
                    rarity to itemList[rarity]!!.plus(item)
                }
                total++
            }
        }
        println("Loaded $total items")

        Rarity.values().filter { it != Rarity.NORMAL && it != Rarity.VOTE }.forEach { rarity ->
            val rankItem = Material.FLOWER_BANNER_PATTERN.itemStack { editMeta { meta -> meta.displayName(text("<color:${rarity.color}><b>Rank</b></color> <dark_gray>×</dark_gray> <gray>${rarity.name}</gray>")) } }
            val keyItem = Material.NAME_TAG.itemStack { editMeta { meta -> meta.displayName(text("<yellow><b>Key</b></yellow> <dark_gray>×</dark_gray> <gray>${rarity.name}</gray>")) } }

            itemList += if(itemList[rarity] == null) {
                rarity to listOf(rankItem, keyItem)
            } else {
                rarity to itemList[rarity]!!.plus(rankItem).plus(keyItem)
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