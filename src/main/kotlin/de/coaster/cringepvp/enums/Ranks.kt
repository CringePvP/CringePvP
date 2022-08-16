package de.coaster.cringepvp.enums

enum class Ranks(val color: String, val cooldownMultiplier: Double) {

    Atmin("#FF0000", 10.0),
    Deffeloppa("#992d22", 10.0),
    Moderador("#e74c3c", 10.0),
    Suporta("#e74c3c", 10.0),
    Bilder("#1f8b4c", 10.0),
    Erdnussbutter("#F8EFBA", 10.0),

    Immortal("#71368a", 10.0),
    Divine("#f1c40f", 7.5),
    Ancient("#c27c0e", 5.0),
    Mythical("#9b59b6", 4.0),
    Legendary("#3498db", 3.0),
    Epic("#1abc9c", 2.5),
    Ultimate("#206694", 2.0),
    Premium("#1f8b4c", 1.5),
    VIP("#e67e22", 1.1),
    Spieler("#2ecc71", 1.0);

    fun isHigherOrEqual(rank: Ranks): Boolean {
        return this.ordinal <= rank.ordinal
    }
}