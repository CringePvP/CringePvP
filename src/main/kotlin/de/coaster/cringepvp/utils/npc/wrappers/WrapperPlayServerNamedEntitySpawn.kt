package de.coaster.cringepvp.utils.npc.wrappers

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import java.util.*


class WrapperPlayServerNamedEntitySpawn : AbstractPacket {
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
    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
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
     * Retrieve Player UUID.
     *
     *
     * Notes: player's UUID
     *
     * @return The current Player UUID
     */
    /**
     * Set Player UUID.
     *
     * @param value - new value.
     */
    var playerUUID: UUID?
        get() = handle.uuiDs.read(0)
        set(value) {
            handle.uuiDs.write(0, value)
        }
    /**
     * Retrieve the position of the spawned entity as a vector.
     *
     * @return The position as a vector.
     */
    /**
     * Set the position of the spawned entity using a vector.
     *
     * @param position - the new position.
     */
    var position: Vector
        get() = Vector(x, y, z)
        set(position) {
            x = position.x
            y = position.y
            z = position.z
        }
    var x: Double
        get() = handle.doubles.read(0)
        set(value) {
            handle.doubles.write(0, value)
        }
    var y: Double
        get() = handle.doubles.read(1)
        set(value) {
            handle.doubles.write(1, value)
        }
    var z: Double
        get() = handle.doubles.read(2)
        set(value) {
            handle.doubles.write(2, value)
        }
    /**
     * Retrieve the yaw of the spawned entity.
     *
     * @return The current Yaw
     */
    /**
     * Set the yaw of the spawned entity.
     *
     * @param value - new yaw.
     */
    var yaw: Float
        get() = handle.bytes.read(0) * 360f / 256.0f
        set(value) {
            handle.bytes.write(0, (value * 256.0f / 360.0f).toInt().toByte())
        }
    /**
     * Retrieve the pitch of the spawned entity.
     *
     * @return The current pitch
     */
    /**
     * Set the pitch of the spawned entity.
     *
     * @param value - new pitch.
     */
    var pitch: Float
        get() = handle.bytes.read(1) * 360f / 256.0f
        set(value) {
            handle.bytes.write(1, (value * 256.0f / 360.0f).toInt().toByte())
        }
    /**
     * Retrieve Metadata.
     *
     *
     * Notes: the client will crash if no metadata is sent
     *
     * @return The current Metadata
     */
    /**
     * Set Metadata.
     *
     * @param value - new value.
     */
    var metadata: WrappedDataWatcher?
        get() = handle.dataWatcherModifier.read(0)
        set(value) {
            handle.dataWatcherModifier.write(0, value)
        }

    companion object {
        val TYPE = PacketType.Play.Server.NAMED_ENTITY_SPAWN
    }
}