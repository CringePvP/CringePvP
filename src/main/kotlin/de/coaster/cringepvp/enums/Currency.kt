package de.coaster.cringepvp.enums

import de.coaster.cringepvp.database.model.CringeUser
import de.moltenKt.unfold.text
import net.kyori.adventure.text.Component
import kotlin.reflect.KMutableProperty1

enum class Currency(val display: Component, val reference: KMutableProperty1<CringeUser, Long> = CringeUser::coins) {

    COINS(text("Coins"), CringeUser::coins),
    GEMS(text("Gems"), CringeUser::gems),
    CRYSTALS(text("Crystals"), CringeUser::crystals),
    RELICS(text("Relics"), CringeUser::relicts),

}