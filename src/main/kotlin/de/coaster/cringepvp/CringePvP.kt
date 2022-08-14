package de.coaster.cringepvp

import de.coaster.cringepvp.database.DatabaseManager
import de.coaster.cringepvp.database.TableUsers
import de.coaster.cringepvp.database.smartTransaction
import de.coaster.cringepvp.managers.CoroutineManager
import de.coaster.cringepvp.managers.PlayerCache
import de.coaster.cringepvp.managers.RegisterManager.registerAll
import de.coaster.cringepvp.placeholders.registerPlaceholders
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.SchemaUtils
import kotlin.system.measureTimeMillis

class CringePvP : JavaPlugin() {

    companion object {
        lateinit var instance: CringePvP
            private set
    }

    init {
        instance = this
    }

    override fun onEnable() {
        // Plugin startup logic

        DatabaseManager.database
        smartTransaction {
            SchemaUtils.create(TableUsers)
        }

        val time = measureTimeMillis {
            registerAll()
            registerPlaceholders()
        }
        println("Plugin enabled in $time ms")
        println("CringePvP is now tweaking your SkyPvP behavior!")
        CoroutineManager
    }

    override fun onDisable() {
        // Plugin shutdown logic
        PlayerCache.saveAll()
    }
}