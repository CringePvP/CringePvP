package de.coaster.cringepvp.enums

import de.coaster.cringepvp.extensions.Currency
import de.coaster.cringepvp.extensions.abbreviate
import de.coaster.cringepvp.utils.toItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.minutes


enum class Kits(val minRank: Ranks = Ranks.Spieler, val kaufPreis: Currency = Currency(0.0, 0), val currencyType: CurrencyType = CurrencyType.COINS, val duration: Duration = INFINITE, val cooldown: Duration, val icon: ItemStack, val items: Array<ItemStack> = arrayOf()) {
    Starter(
        cooldown = 5.minutes,
        icon = Material.LEATHER_CHESTPLATE.toItemBuilder { display("<#e67e22>Starter Kit") }.build(),
        items = arrayOf(
            Material.LEATHER_HELMET.toItemBuilder().build(),
            Material.LEATHER_CHESTPLATE.toItemBuilder().build(),
            Material.LEATHER_LEGGINGS.toItemBuilder().build(),
            Material.LEATHER_BOOTS.toItemBuilder().build(),
            Material.WOODEN_SWORD.toItemBuilder().build(),
            Material.BREAD.toItemBuilder().asQuantity(16).build(),
        )
    ),
    Miner(
        kaufPreis = 10 abbreviate 0,
        currencyType = CurrencyType.CRYSTALS,
        cooldown = 15.minutes,
        icon = Material.DIAMOND_PICKAXE.toItemBuilder { display("<#e67e22>Miner Kit") }.build(),
        items = arrayOf(
            Material.STONE_PICKAXE.toItemBuilder { display("<#e67e22>Spitzhacke") }.build(),
        )
    ),
    Archer(
        kaufPreis = 100 abbreviate 3,
        minRank = Ranks.Premium,
        cooldown = 10.minutes,
        icon = Material.BOW.toItemBuilder { display("<#e67e22>Archer Kit") }.build(),
        items = arrayOf(
            Material.ARROW.toItemBuilder { display("<#e67e22>Arrow") }.asQuantity(16).build(),
            Material.SPECTRAL_ARROW.toItemBuilder { display("<#e67e22>Sichtbarmach Pfeil") }.build(),
            Material.BOW.toItemBuilder { display("<#e67e22>Bow") }.build().asOne()
        )
    ),
    Carl(
        kaufPreis = 500 abbreviate 10,
        currencyType = CurrencyType.GEMS,
        cooldown = 30.minutes,
        icon = Material.LLAMA_SPAWN_EGG.toItemBuilder { display("<#e67e22>Carl Kit") }.build(),
        items = arrayOf(

            Material.DIAMOND_SWORD.toItemBuilder { display("<#e67e22>Schwert") }.build(),
            Material.DIAMOND_AXE.toItemBuilder { display("<#e67e22>Axt") }.build(),
            Material.ARROW.toItemBuilder { display("<#e67e22>Arrow") }.asQuantity(16).build(),
            Material.SPECTRAL_ARROW.toItemBuilder { display("<#e67e22>Sichtbarmach Pfeil")  }.build(),
            Material.BOW.toItemBuilder { display("<#e67e22>Bow") }.build(),
            Material.ENDER_PEARL.toItemBuilder { display("<#e67e22>Enderperle") }.asQuantity(16).build(),
            Material.GOLDEN_APPLE.toItemBuilder { display("<#e67e22>Goldener Apfel") }.asQuantity(5).build(),
            Material.COOKED_BEEF.toItemBuilder { display("<#e67e22>Steak") }.asQuantity(64).build(),
        )
    ),
}