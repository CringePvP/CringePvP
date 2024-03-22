package de.coaster.cringepvp.enums

import de.coaster.cringepvp.extensions.Currency
import de.coaster.cringepvp.extensions.abbreviate
import de.fruxz.sparkle.framework.extension.visual.ui.itemStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.minutes


enum class Kits(val minRank: Ranks = Ranks.Spieler, val kaufPreis: Currency = Currency(0.0, 0), val currencyType: CurrencyType = CurrencyType.COINS, val duration: Duration = INFINITE, val cooldown: Duration, val icon: ItemStack, val items: Array<ItemStack> = arrayOf()) {
    Starter(
        cooldown = 5.minutes,
        icon = Material.LEATHER_CHESTPLATE.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Starter Kit")) } },
        items = arrayOf(
            Material.LEATHER_HELMET.itemStack,
            Material.LEATHER_CHESTPLATE.itemStack,
            Material.LEATHER_LEGGINGS.itemStack,
            Material.LEATHER_BOOTS.itemStack,
            Material.WOODEN_SWORD.itemStack,
            Material.BREAD.itemStack.asQuantity(16),
        )
    ),
    Miner(
        kaufPreis = 10 abbreviate 0,
        currencyType = CurrencyType.CRYSTALS,
        cooldown = 15.minutes,
        icon = Material.DIAMOND_PICKAXE.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Miner Kit")) } },
        items = arrayOf(
            Material.STONE_PICKAXE.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Spitzhacke")) } },
        )
    ),
    Archer(
        kaufPreis = 100 abbreviate 3,
        minRank = Ranks.Premium,
        cooldown = 10.minutes,
        icon = Material.BOW.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Archer Kit")) } },
        items = arrayOf(
            Material.ARROW.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Arrow")) } }.asQuantity(16),
            Material.SPECTRAL_ARROW.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Sichtbarmach Pfeil")) } }.asOne(),
            Material.BOW.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Bow")) } }.asOne()
        )
    ),
    Carl(
        kaufPreis = 500 abbreviate 10,
        currencyType = CurrencyType.GEMS,
        cooldown = 30.minutes,
        icon = Material.LLAMA_SPAWN_EGG.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Carl Kit")) } },
        items = arrayOf(

            Material.DIAMOND_SWORD.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Schwert")) } },
            Material.DIAMOND_AXE.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Axt")) } },
            Material.ARROW.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Arrow")) } }.asQuantity(16),
            Material.SPECTRAL_ARROW.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Sichtbarmach Pfeil")) } }.asOne(),
            Material.BOW.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Bow")) } }.asOne(),
            Material.ENDER_PEARL.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Enderperle")) } }.asQuantity(16),
            Material.GOLDEN_APPLE.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Goldener Apfel")) } }.asQuantity(5),
            Material.COOKED_BEEF.itemStack { editMeta { meta-> meta.displayName(de.fruxz.stacked.text("<#e67e22>Steak")) } }.asQuantity(64),
        )
    ),
}