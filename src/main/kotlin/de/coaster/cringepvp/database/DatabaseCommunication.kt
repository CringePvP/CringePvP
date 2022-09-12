package de.coaster.cringepvp.database

import de.coaster.cringepvp.database.TableUsers.userUUID
import de.coaster.cringepvp.database.model.CringeUser
import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.extensions.abbreviate
import de.moltenKt.core.tool.timing.calendar.Calendar
import org.jetbrains.exposed.sql.*
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.time.Duration.Companion.seconds

private fun Instant.toCalendar() =
    Calendar(GregorianCalendar.from(ZonedDateTime.from(this.atZone(ZoneId.systemDefault()))))

fun getCringeUserOrNull(uuid: UUID): CringeUser? = smartTransaction {
    return@smartTransaction TableUsers.select { userUUID eq uuid.toString() }.firstOrNull()?.let(::mapToCringeUser)
}

fun getCringeUserTitles(uuid: UUID): Set<Titles> = smartTransaction {
    return@smartTransaction TableUserTitles.select { TableUserTitles.userUUID eq uuid.toString() }.mapNotNull { resultRow ->
        Titles.values().find { it.name.equals(resultRow[TableUserTitles.title], true) }
    }.toSet()
}

private fun mapToCringeUser(resultRow: ResultRow): CringeUser = with(resultRow) {
    return CringeUser(
        uuid = UUID.fromString(this[userUUID]),
        username = this[TableUsers.userName],
        xp = this[TableUsers.userXP],
        rank = Ranks.values().find { it.name.equals(this[TableUsers.userRank], true) } ?: Ranks.Spieler,
        title = Titles.values().find { it.name.equals(this[TableUsers.userTitle], true) } ?: Titles.NoTITLE,
        ownedTitles = getCringeUserTitles(UUID.fromString(this[userUUID])),
        coins = this[TableUsers.userCoins] abbreviate this[TableUsers.userCoinsAbbreviatedIndex],
        gems = this[TableUsers.userGems] abbreviate this[TableUsers.userGemsAbbreviatedIndex],
        crystals = this[TableUsers.userCrystals] abbreviate this[TableUsers.userCrystalsAbbreviatedIndex],
        relicts = this[TableUsers.userRelicts] abbreviate this[TableUsers.userRelictsAbbreviatedIndex],
        kills = this[TableUsers.userKills],
        deaths = this[TableUsers.userDeaths],
        baseAttack = this[TableUsers.userBaseAttack],
        baseDefense = this[TableUsers.userBaseDefense],
        baseSpeed = this[TableUsers.userBaseSpeed],
        baseHealth = this[TableUsers.userBaseHealth],
        votes = this[TableUsers.userVotes],
        voteKeys = this[TableUsers.userVoteKeys],
        vipKeys = this[TableUsers.userVipKeys],
        normalKeys = this[TableUsers.userNormalKeys],
        premiumKeys = this[TableUsers.userPremiumKeys],
        ultimateKeys = this[TableUsers.userUltimateKeys],
        epicKeys = this[TableUsers.userEpicKeys],
        legendaryKeys = this[TableUsers.userLegendaryKeys],
        mythicalKeys = this[TableUsers.userMythicalKeys],
        ancientKeys = this[TableUsers.userAncientKeys],
        divineKeys = this[TableUsers.userDivineKeys],
        immortalKeys = this[TableUsers.userImmortalKeys],

        steinbruchLevel = this[TableUsers.steinbruchLevel],

        firstJoined = this[TableUsers.userFirstJoined].toCalendar(),
        lastTimeJoined = this[TableUsers.userLastJoined].toCalendar(),
        onlineTime = this[TableUsers.onlineTime].seconds,
    )
}

fun createCringeUser(cringeUser: CringeUser): CringeUser = smartTransaction {
    TableUsers.insert {
        it[userUUID] = cringeUser.uuid.toString()
        it[userName] = cringeUser.username
    }
    return@smartTransaction cringeUser
}

fun updateCringeUserDB(cringeUser: CringeUser) = smartTransaction {
    TableUsers.update({ userUUID eq cringeUser.uuid.toString() }) {
        it[userName] = cringeUser.username
        it[userXP] = cringeUser.xp
        it[userRank] = cringeUser.rank.name
        it[userTitle] = cringeUser.title.name
        it[userCoins] = cringeUser.coins.value
        it[userCoinsAbbreviatedIndex] = cringeUser.coins.abbreviationIndex
        it[userGems] = cringeUser.gems.value
        it[userGemsAbbreviatedIndex] = cringeUser.gems.abbreviationIndex
        it[userCrystals] = cringeUser.crystals.value
        it[userCrystalsAbbreviatedIndex] = cringeUser.crystals.abbreviationIndex
        it[userRelicts] = cringeUser.relicts.value
        it[userRelictsAbbreviatedIndex] = cringeUser.relicts.abbreviationIndex
        it[userKills] = cringeUser.kills
        it[userDeaths] = cringeUser.deaths
        it[userBaseAttack] = cringeUser.baseAttack
        it[userBaseDefense] = cringeUser.baseDefense
        it[userBaseSpeed] = cringeUser.baseSpeed
        it[userBaseHealth] = cringeUser.baseHealth
        it[userVotes] = cringeUser.votes
        it[userVoteKeys] = cringeUser.voteKeys
        it[userVipKeys] = cringeUser.vipKeys
        it[userNormalKeys] = cringeUser.normalKeys
        it[userPremiumKeys] = cringeUser.premiumKeys
        it[userUltimateKeys] = cringeUser.ultimateKeys
        it[userEpicKeys] = cringeUser.epicKeys
        it[userLegendaryKeys] = cringeUser.legendaryKeys
        it[userMythicalKeys] = cringeUser.mythicalKeys
        it[userAncientKeys] = cringeUser.ancientKeys
        it[userDivineKeys] = cringeUser.divineKeys
        it[userImmortalKeys] = cringeUser.immortalKeys

        it[steinbruchLevel] = cringeUser.steinbruchLevel

        it[userFirstJoined] = cringeUser.firstJoined.javaInstant
        it[userLastJoined] = cringeUser.lastTimeJoined.javaInstant
        it[onlineTime] = cringeUser.onlineTime.inWholeSeconds
    }
    cringeUser.ownedTitles.forEach { titles ->
        TableUserTitles.replace {
            it[userUUID] = cringeUser.uuid.toString()
            it[title] = titles.name
        }
    }
}