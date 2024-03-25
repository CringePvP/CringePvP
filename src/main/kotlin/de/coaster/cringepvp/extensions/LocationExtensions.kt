package de.coaster.cringepvp.extensions

import org.bukkit.Location

val Location.distanceToZero
    get() = distance(Location(world, 0.0, 0.0, 0.0))