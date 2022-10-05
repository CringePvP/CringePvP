package de.coaster.cringepvp.enums

import de.coaster.cringepvp.database.model.CringeUser
import de.coaster.cringepvp.extensions.Currency
import de.fruxz.stacked.text
import net.kyori.adventure.text.Component
import kotlin.reflect.KMutableProperty1

enum class CurrencyType(val display: Component, val reference: KMutableProperty1<CringeUser, Currency> = CringeUser::coins) {

    COINS(text("Coins"), CringeUser::coins),
    GEMS(text("Gems"), CringeUser::gems),
    CRYSTALS(text("Crystals"), CringeUser::crystals),
    RELICS(text("Relics"), CringeUser::relicts),

}