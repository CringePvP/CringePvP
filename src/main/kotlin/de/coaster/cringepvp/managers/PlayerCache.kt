package de.coaster.cringepvp.managers

import de.coaster.cringepvp.database.model.CringeUser
import de.coaster.cringepvp.database.updateCringeUserDB
import de.coaster.cringepvp.extensions.toCringeUserDB
import java.util.*

object PlayerCache {

    private var cache = mapOf<UUID, CringeUser>()

    fun get(uuid: UUID): CringeUser = cache[uuid] ?: register(uuid.toCringeUserDB())

    fun all(): Set<CringeUser> = cache.values.toSet()

    private fun register(user: CringeUser): CringeUser {
        cache += Pair(user.uuid, user)
        return user
    }

    fun updateCringeUser(cringeUser: CringeUser): CringeUser {
        cache -= cringeUser.uuid
        cache += Pair(cringeUser.uuid, cringeUser)
        return cringeUser
    }

    fun remove(uuid: UUID) {
        val user = cache[uuid]
        if (user != null) {
            cache -= uuid
            updateCringeUserDB(user)
        }
    }

    fun saveAll() {
        cache.values.forEach { updateCringeUserDB(it) }
    }
}