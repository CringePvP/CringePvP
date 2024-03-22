package de.coaster.cringepvp.utils.npc.wrappers

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.WrappedWatchableObject
import org.bukkit.World
import org.bukkit.entity.Entity


class WrapperPlayServerEntityMetadata : AbstractPacket {
    constructor() : super(PacketContainer(TYPE), TYPE) {
        handle.modifier.writeDefaults()
    }

    constructor(packet: PacketContainer?) : super(packet, TYPE)
    /**
     * Retrieve Entity ID.
     *
     *
     * Notes: entity's ID
     *
     * @return The current Entity ID
     */
    /**
     * Set Entity ID.
     *
     * @param value - new value.
     */
    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }

    /**
     * Retrieve the entity of the painting that will be spawned.
     *
     * @param world - the current world of the entity.
     * @return The spawned entity.
     */
    fun getEntity(world: World): Entity {
        return handle.getEntityModifier(world).read(0)
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     *
     * @param event - the packet event.
     * @return The spawned entity.
     */
    fun getEntity(event: PacketEvent): Entity {
        return getEntity(event.player.world)
    }
    /**
     * Retrieve Metadata.
     *
     * @return The current Metadata
     */
    /**
     * Set Metadata.
     *
     * @param value - new value.
     */
    var metadata: List<WrappedWatchableObject?>?
        get() = handle.watchableCollectionModifier.read(0)
        set(value) {
            handle.watchableCollectionModifier.write(0, value)
        }

    companion object {
        val TYPE = PacketType.Play.Server.ENTITY_METADATA
    }
}