package de.coaster.cringepvp

import de.coaster.cringepvp.database.DatabaseManager
import de.coaster.cringepvp.database.TableUsers
import de.coaster.cringepvp.database.smartTransaction
import de.coaster.cringepvp.managers.CoroutineManager
import de.coaster.cringepvp.managers.ItemManager
import de.coaster.cringepvp.managers.PlayerCache
import de.coaster.cringepvp.managers.RegisterManager.registerAll
import de.coaster.cringepvp.placeholders.registerPlaceholders
import de.moltenKt.core.extension.empty
import de.moltenKt.core.tool.smart.identification.Identity
import de.moltenKt.paper.extension.tasky.delayed
import de.moltenKt.paper.extension.tasky.doSync
import de.moltenKt.paper.structure.app.App
import de.moltenKt.paper.structure.app.AppCache
import de.moltenKt.paper.structure.app.AppCompanion
import de.moltenKt.paper.structure.app.cache.CacheDepthLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.SchemaUtils
import java.util.*
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.seconds

class CringePvP : JavaPlugin() {

    companion object {
        lateinit var instance: CringePvP
            private set

        lateinit var coroutineScope: CoroutineScope
            private set
    }

    init {
        instance = this
        coroutineScope = CoroutineScope(Dispatchers.Default)
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
        ItemManager
        CoroutineManager
    }

    override fun onDisable() {
        PlayerCache.saveAll()
        coroutineScope.coroutineContext.cancelChildren()
    }
}