package de.coaster.cringepvp.database

import de.coaster.cringepvp.database.TableUsers.userUUID
import de.coaster.cringepvp.database.model.CringeUser
import de.moltenKt.core.tool.timing.calendar.Calendar
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
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

private fun mapToCringeUser(resultRow: ResultRow): CringeUser = with(resultRow){
    return CringeUser(
        uuid = UUID.fromString(this[userUUID]),
        username = this[TableUsers.userName],
        xp = this[TableUsers.userXP],
        rank = this[TableUsers.userRank],
        title = this[TableUsers.userTitle],
        coins = this[TableUsers.userCoins],
        gems = this[TableUsers.userGems],
        crystals = this[TableUsers.userCrystals],
        relicts = this[TableUsers.userRelicts],
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
        it[userRank] = cringeUser.rank
        it[userTitle] = cringeUser.title
        it[userCoins] = cringeUser.coins
        it[userGems] = cringeUser.gems
        it[userCrystals] = cringeUser.crystals
        it[userRelicts] = cringeUser.relicts
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
        it[userFirstJoined] = cringeUser.firstJoined.javaInstant
        it[userLastJoined] = cringeUser.lastTimeJoined.javaInstant
        it[onlineTime] = cringeUser.onlineTime.inWholeSeconds
    }
}