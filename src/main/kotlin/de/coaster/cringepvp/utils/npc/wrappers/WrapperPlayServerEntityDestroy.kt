package de.coaster.cringepvp.utils.npc.wrappers

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer


class WrapperPlayServerEntityDestroy : AbstractPacket {
    constructor() : super(PacketContainer(TYPE), TYPE) {
        handle.modifier.writeDefaults()
    }

    constructor(packet: PacketContainer?) : super(packet, TYPE)

    /**
     * Retrieve Count.
     *
     *
     * Notes: length of following array
     *
     * @return The current Count
     */
    fun getCount(): Int {
        return handle.integerArrays.read(0).size
    }

    /**
     * Retrieve Entity IDs.
     *
     *
     * Notes: the list of entities of destroy
     *
     * @return The current Entity IDs
     */
    fun getEntityIDs(): IntArray {
        return handle.integerArrays.read(0)
    }

    /**
     * Set Entity IDs.
     *
     * @param value - new value.
     */
    fun setEntityIds(value: IntArray?) {
        handle.integerArrays.write(0, value)
    }

    companion object {
        val TYPE = PacketType.Play.Server.ENTITY_DESTROY
    }
}