package de.coaster.cringepvp.utils.npc.wrappers

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.Vector


class WrapperPlayClientUseEntity : AbstractPacket {
    constructor() : super(PacketContainer(TYPE), TYPE) {
        handle.modifier.writeDefaults()
    }

    constructor(packet: PacketContainer?) : super(packet, TYPE)
    /**
     * Retrieve entity ID of the target.
     *
     * @return The current entity ID
     */
    /**
     * Set entity ID of the target.
     *
     * @param value - new value.
     */
    var targetID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }

    /**
     * Retrieve the entity that was targeted.
     *
     * @param world - the current world of the entity.
     * @return The targeted entity.
     */
    fun getTarget(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    /**
     * Retrieve the entity that was targeted.
     *
     * @param event - the packet event.
     * @return The targeted entity.
     */
    fun getTarget(event: PacketEvent): Entity {
        return getTarget(event.player.world)
    }
    /**
     * Retrieve Type.
     *
     * @return The current Type
     */
    /**
     * Set Type.
     *
     * @param value - new value.
     */
    var type: EntityUseAction?
        get() = handle.entityUseActions.read(0)
        set(value) {
            handle.entityUseActions.write(0, value)
        }
    /**
     * Retrieve the target vector.
     *
     *
     * Notes: Only if [.getType] is [EntityUseAction.INTERACT_AT].
     *
     * @return The target vector or null
     */
    /**
     * Set the target vector.
     *
     * @param value - new value.
     */
    var targetVector: Vector?
        get() = handle.vectors.read(0)
        set(value) {
            handle.vectors.write(0, value)
        }

    companion object {
        val TYPE = PacketType.Play.Client.USE_ENTITY
    }
}