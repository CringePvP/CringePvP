package de.coaster.cringepvp.enums

enum class Titles(val display: String, val rarity: Rarity = Rarity.NORMAL, val description: String = "") {

    MonsterSchlachter("Monster Schlachter", Rarity.NORMAL, "Töte 100 Monster"),
    GameMaster("Game Master", Rarity.EXCLUSIVE, "Den GameMastern des Servers vorbehalten."),
    MystischerMagier("Mystischer Magier", Rarity.MYTHICAL),
    BETATester("BETA Tester", Rarity.EXCLUSIVE, "Du hast bei der BETA-Phase des Servers teilgenommen.\nVielen Dank für deine Unterstützung!"),
    FIRST_VOTE("Vote Unterstützer", Rarity.EXCLUSIVE, "Du hast den Server mit einem Vote unterstützt! \nVielen Dank für deine Unterstützung!"),
    HEAVY_VOTER("Vielvoter", Rarity.EXCLUSIVE, "Du hast den Server mit mehr als 10 Votes unterstützt! \nDu scheinst die Rewards ja echt nötig zu haben."),
    NoTITLE("Neuer Noob")

}