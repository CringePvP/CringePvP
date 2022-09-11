package de.coaster.cringepvp.utils.npc.wrappers

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction
import com.comphenix.protocol.wrappers.PlayerInfoData


class WrapperPlayServerPlayerInfo : AbstractPacket {
    constructor() : super(PacketContainer(TYPE), TYPE) {
        handle.modifier.writeDefaults()
    }

    constructor(packet: PacketContainer?) : super(packet, TYPE)

    var action: PlayerInfoAction?
        get() = handle.playerInfoAction.read(0)
        set(value) {
            handle.playerInfoAction.write(0, value)
        }
    var data: List<PlayerInfoData?>?
        get() = handle.playerInfoDataLists.read(0)
        set(value) {
            handle.playerInfoDataLists.write(0, value)
        }

    companion object {
        val TYPE = PacketType.Play.Server.PLAYER_INFO
    }
}