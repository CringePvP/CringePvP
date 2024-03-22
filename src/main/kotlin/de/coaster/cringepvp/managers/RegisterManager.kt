package de.coaster.cringepvp.managers

import de.coaster.cringepvp.CringePvP
import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.extensions.toCringeUser
import me.neznamy.tab.api.TabAPI
import me.neznamy.tab.api.event.player.PlayerLoadEvent
import me.neznamy.tab.api.nametag.UnlimitedNameTagManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.permissions.Permission
import org.bukkit.plugin.Plugin
import org.reflections8.Reflections
import kotlin.system.measureTimeMillis

object RegisterManager {

    /**
     * Register all commands and listeners from the plugin
     */
    fun registerAll() {
        val reflections = Reflections("de.coaster.cringepvp")

        val timeListeners = measureTimeMillis {
            for (clazz in reflections.getSubTypesOf(Listener::class.java)) {
                try {
                    val constructor = clazz.getDeclaredConstructor()

                    constructor.isAccessible = true

                    val event = constructor.newInstance() as Listener

                    Bukkit.getPluginManager().registerEvents(event, CringePvP.instance)
                    Bukkit.getConsoleSender()
                        .sendMessage("Listener ${event.javaClass.simpleName} registered")
                } catch (exception: InstantiationError) {
                    exception.printStackTrace()
                } catch (exception: IllegalAccessException) {
                    exception.printStackTrace()
                }
            }
        }
        println("Registered listeners in $timeListeners ms")

        val timeCommands = measureTimeMillis {
            for (clazz in reflections.getTypesAnnotatedWith(RegisterCommand::class.java)) {
                try {
                    val annotation: RegisterCommand = clazz.getAnnotation(RegisterCommand::class.java)

                    val pluginClass: Class<PluginCommand> = PluginCommand::class.java
                    val constructor = pluginClass.getDeclaredConstructor(String::class.java, Plugin::class.java)

                    constructor.isAccessible = true

                    val command: PluginCommand = constructor.newInstance(annotation.name, CringePvP.instance)

                    command.aliases = annotation.aliases.toList()
                    command.description = annotation.description
                    if(annotation.permission != "") {
                        command.permission = Permission(annotation.permission, annotation.permissionDefault).name
                    }
                    command.setExecutor(clazz.getDeclaredConstructor().newInstance() as CommandExecutor)

                    Bukkit.getCommandMap().register(CringePvP.instance.name.lowercase(), command)
                    Bukkit.getConsoleSender().sendMessage("Command ${command.name} registered")
                } catch (exception: InstantiationError) {
                    exception.printStackTrace()
                } catch (exception: IllegalAccessException) {
                    exception.printStackTrace()
                }
            }
        }
        println("Registered commands in $timeCommands ms")


        TabAPI.getInstance().eventBus?.register(
            PlayerLoadEvent::class.java
        ) { event: PlayerLoadEvent ->
            val tabPlayer = event.player
            val cringeUser = (tabPlayer.player as Player).toCringeUser()
            val nametagManager = TabAPI.getInstance().nameTagManager as UnlimitedNameTagManager
            nametagManager.setName(tabPlayer, "#a4b0be${tabPlayer.name}")
            nametagManager.setLine(tabPlayer, "rank", "#7bed9f・ %color%%rank% #7bed9f・")
            nametagManager.setLine(tabPlayer, "title", "#eccc68・ %title% ・")
            nametagManager.setLine(tabPlayer, "level", "#ff6b81Level × ${cringeUser.level}")
        }
    }
}