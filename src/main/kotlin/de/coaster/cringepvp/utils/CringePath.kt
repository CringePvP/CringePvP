package de.coaster.cringepvp.utils

import de.coaster.cringepvp.extensions.toCringeLocation
import de.coaster.cringepvp.managers.CoroutineManager
import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class CringePath(vararg locationPoints: Location) {

    companion object {
        fun fromConfig(config: ConfigurationSection, configKey: String) : CringePath {
            val locationAny = config.get(configKey)
            if (locationAny is List<*>) {
                val locations = locationAny.map { (it as String).toCringeLocation() }
                return CringePath(*locations.toTypedArray())
            }
            else if(locationAny is String) {
                val location = locationAny.toCringeLocation()
                return CringePath(location)
            }
            else {
                throw IllegalArgumentException("Invalid path format")
            }
        }
    }

    val locations = locationPoints.toList()
    val steps = locations.size

    class Progress(val player: Player, private val path: CringePath, step: Int = 0) {
        var currentStep = step

        private fun nextStep() {
            currentStep++
        }

        private fun isFinished() : Boolean {
            return currentStep >= path.steps
        }

        private fun getCurrentLocation() : Location {
            return path.locations[currentStep]
        }

        fun startTraveling() {
            /* Apply velocity to player to fly to the landingLocation */
            player.velocity = getCurrentLocation().toVector().subtract(player.location.toVector()).add(Vector(0.0, 5.0, 0.0))
            CoroutineManager.startCoroutine {
                while (!isFinished()) {
                    while (!player.isDead && player.location.distance(getCurrentLocation()) > 5.0) {
                        player.velocity = getCurrentLocation().toVector().subtract(player.location.toVector()).multiply(0.2)
                        delay(100)
                    }
                    nextStep()
                }
            }
        }
    }
}