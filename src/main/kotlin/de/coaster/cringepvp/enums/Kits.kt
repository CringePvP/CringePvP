package de.coaster.cringepvp.enums

import de.moltenKt.paper.extension.display.ui.itemStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


enum class Kits(val minRank: Ranks = Ranks.Spieler, val kaufPreis: Int = 0, val currency: Currency = Currency.COINS, val duration: Duration = INFINITE, val cooldown: Duration, val icon: ItemStack, val items: Array<ItemStack> = arrayOf()) {
    Starter(
        cooldown = 10.minutes,
        icon = Material.LEATHER_CHESTPLATE.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Starter Kit")) } }
    ),
    Miner(
        kaufPreis = 10,
        currency = Currency.GEMS,
        cooldown = 15.minutes,
        icon = Material.DIAMOND_PICKAXE.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Miner Kit")) } },
        items = arrayOf(
            Material.STONE_PICKAXE.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Spitzhacke")) } },
        )
    ),
    Carl(
        kaufPreis = 500,
        currency = Currency.GEMS,
        cooldown = 30.minutes,
        icon = Material.LLAMA_SPAWN_EGG.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Carl Kit")) } },
        items = arrayOf(

            Material.DIAMOND_SWORD.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Schwert")) } },
            Material.DIAMOND_AXE.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Axt")) } },
            Material.ARROW.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Arrow")) } }.asQuantity(16),
            Material.SPECTRAL_ARROW.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Sichtbarmach Pfeil")) } }.asOne(),
            Material.BOW.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Bow")) } }.asOne(),
            Material.ENDER_PEARL.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Enderperle")) } }.asQuantity(16),
            Material.GOLDEN_APPLE.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Enderperle")) } }.asQuantity(5),
            Material.COOKED_BEEF.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Enderperle")) } }.asQuantity(64),
            Material.LLAMA_SPAWN_EGG.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>CARL")) } }.asQuantity(1),
        )
    ),
    Archer(
        kaufPreis = 100,
        duration = 2.hours,
        cooldown = 10.minutes,
        icon = Material.BOW.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Archer Kit")) } },
        items = arrayOf(
            Material.ARROW.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Arrow")) } }.asQuantity(16),
            Material.SPECTRAL_ARROW.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Sichtbarmach Pfeil")) } }.asOne(),
            Material.BOW.itemStack { editMeta { meta-> meta.displayName(de.moltenKt.unfold.text("<#e67e22>Bow")) } }.asOne()
        )
    ),
}