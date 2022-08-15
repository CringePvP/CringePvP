package de.coaster.cringepvp.enums

enum class Rarity(val rarity: Int, val color: String) {
    NORMAL(50, "#c9a368"),
    VIP(39, "#bb8b69"),
    PREMIUM(28, "#7e5c3c"),
    ULTIMATE(27, "#6691d7"),
    EPIC(26, "#bababa"),
    LEGENDARY(15, "#938957"),
    MYTHICAL(9, "#446f1b"),
    ANCIENT(6, "#9e4622"),
    DIVINE(3, "#debb5a"),
    IMMORTAL(1, "#e79800"),
    VOTE(0, "#dabc50");

    fun getAllBelow(): List<Rarity> {
        return values().filter { it.rarity >= this.rarity }
    }
}