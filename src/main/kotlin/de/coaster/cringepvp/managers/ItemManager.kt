package de.coaster.cringepvp.managers

import de.coaster.cringepvp.enums.Rarity
import de.coaster.cringepvp.extensions.plainText
import de.coaster.cringepvp.utils.FileConfig
import dev.fruxz.stacked.text
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

object ItemManager {

    var items = mapOf<String, List<ItemStack>>()
    var raritiesWithWeights = listOf<Rarity>()

    init {
        println("ItemManager loaded")
        registerItems()
        updateWeights()
    }

    private fun updateWeights() {
        val rarities = Rarity.values().filter { items[it.name]?.isNotEmpty() ?: false }

        // Get a random rarity based on the rarity chance
        val weights = rarities.map { it.rarity }
        raritiesWithWeights = listOf()
        for (i in weights.indices) {
            for (j in 0 until weights[i]) {
                raritiesWithWeights += rarities[i]
            }
        }
    }

    private fun registerItems() {
        println("Registering items")
        items = mapOf()
        Rarity.values().forEach { rarity ->
            val config = FileConfig("items/${rarity.name.lowercase(Locale.getDefault())}.yml")
            if (!config.contains("itemNames")) {
                config.set("itemNames", listOf<String>())
                config.saveConfig()
            }
            val itemList = config.getStringList("itemNames")
            val itemStacks = itemList.mapNotNull { config.getItemStack(it) }
            items += Pair(rarity.name, itemStacks)
        }
        val amount = items.values.map { it.size }.sum()
        println("Registered $amount items")
    }

    private fun ItemStack.addRarityToLore(rarity: Rarity): ItemStack {
        this.editMeta { meta ->
            meta.lore(listOf(text("<#aaaaaa>Rarity: <${rarity.color}>${rarity.name}")))
        }
        return this
    }

    private fun getItem(rarity: Rarity): ItemStack {
        val itemList = items[rarity.name] ?: return ItemStack(Material.AIR)
        return if(itemList.isNotEmpty()) itemList.random().addRarityToLore(rarity) else ItemStack(Material.AIR)
    }

    fun getItems(rarity: Rarity): List<ItemStack> {
        return items[rarity.name]?.map { it.addRarityToLore(rarity) } ?: return listOf()
    }

    fun getItems(rarity: List<Rarity>): List<ItemStack> {
        return rarity.map { getItems(it) }.flatten()
    }

    // Get a random item from the rarities with at least one item from the minOneRarity list
    fun getRandomItemsWithOneMinRarity(amount: Int, rarities: List<Rarity>, minOneRarity: Rarity): List<ItemStack> {
        return getItems(rarities).shuffled().take(amount - 1) + getItem(minOneRarity)
    }

    private fun getItem(): ItemStack {
        val rarity = raritiesWithWeights.random()
        return getItem(rarity)
    }

    fun getItems(amount: Int): Array<ItemStack> {
        val items = arrayOfNulls<ItemStack>(amount)
        for (i in 0 until amount) {
            items[i] = getItem()
        }
        return items.filterNotNull().toTypedArray()
    }

    fun addItem(rarity: Rarity, item: ItemStack) {
        var itemList = items[rarity.name] ?: return
        itemList += item
        items += Pair(rarity.name, itemList)

        val config = FileConfig("items/${rarity.name.lowercase(Locale.getDefault())}.yml")
        val itemNames = config.getStringList("itemNames")
        val itemName = if (item.itemMeta.hasDisplayName()) item.itemMeta.displayName else item.type.name
        itemNames += itemName
        config.set("itemNames", itemNames)
        config.set(itemName, item)
        config.saveConfig()
        updateWeights()
    }

    fun addItems(rarity: Rarity, multipleItems: List<ItemStack>) {
        var itemList = items[rarity.name] ?: return
        itemList += multipleItems
        items += Pair(rarity.name, itemList)

        val config = FileConfig("items/${rarity.name.lowercase(Locale.getDefault())}.yml")
        val itemNames = config.getStringList("itemNames")
        for (item in multipleItems) {
            val itemName = (if (item.itemMeta.hasDisplayName()) item.itemMeta.displayName()!!.plainText else item.type.name) + "#" + item.amount
            itemNames += itemName
            config.set(itemName, item)
        }
        config.set("itemNames", itemNames)
        config.saveConfig()
        updateWeights()
    }
}