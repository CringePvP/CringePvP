package de.coaster.cringepvp.enums

import de.coaster.cringepvp.database.model.CringeUser
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

enum class Keys(val playerReference: KMutableProperty1<CringeUser, Long>, val dropAmount: Int) {
    NORMAL(CringeUser::normalKeys, 10),
    VIP(CringeUser::vipKeys, 10),
    PREMIUM(CringeUser::premiumKeys, 10),
    ULTIMATE(CringeUser::ultimateKeys, 10),
    EPIC(CringeUser::epicKeys, 10),
    LEGENDARY(CringeUser::legendaryKeys, 10),
    MYTHICAL(CringeUser::mythicalKeys, 10),
    ANCIENT(CringeUser::ancientKeys, 10),
    DIVINE(CringeUser::divineKeys, 10),
    IMMORTAL(CringeUser::immortalKeys, 10),
    VOTE(CringeUser::voteKeys, 15);

    fun getAllBelow(): List<Keys> {
        return values().filter { it.ordinal <= this.ordinal }
    }
}