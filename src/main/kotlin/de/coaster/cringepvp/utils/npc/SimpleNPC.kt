package de.coaster.cringepvp.utils.npc

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.*
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import de.coaster.cringepvp.CringePvP
import de.coaster.cringepvp.utils.npc.wrappers.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer


class SimpleNPC {
    private val entityID // unique entityID the server holds to find/modify existing entities
            : Int = UUID.randomUUID().clockSequence()
    private var gameProfile: WrappedGameProfile = WrappedGameProfile(UUID.randomUUID(), "§7NPC-$entityID")
    private var location: Location? = null
    private var hideNameTag = false
    private var collision = true
    private var hideFromTabList = false

    init {
        if (!listenerRegistered) {
            listenerRegistered = true
            startListener()
        }
    }

    fun show(player: Player) {
        Bukkit.getScheduler().runTaskAsynchronously(CringePvP.instance, Runnable {
            sendPlayerInfoPacket(EnumWrappers.PlayerInfoAction.ADD_PLAYER, player)
            sendNamedEntitySpawnPacket(player)
            sendEntityHeadRotation(player)
            sendEntityMetadataPacket(17, (0x01 or 0x02 or 0x04 or 0x08 or 0x10 or 0x20 or 0x40).toByte(), player)
            sendScoreboardTeamPacket(player)
            if (hideFromTabList) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(
                    CringePvP.instance,
                    Runnable {
                        if (player.isOnline) sendPlayerInfoPacket(
                            EnumWrappers.PlayerInfoAction.REMOVE_PLAYER,
                            player
                        )
                    }, 80L
                )
            }
        })
    }

    fun show(players: Collection<Player>) {
        players.forEach(Consumer { player: Player -> this.show(player) })
    }

    fun hide(player: Player) {
        Bukkit.getScheduler().runTaskAsynchronously(CringePvP.instance, Runnable {
            sendPlayerInfoPacket(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER, player)
            sendEntityDestroyPacket(player)
        })
    }

    fun hide(players: Collection<Player>) {
        players.forEach(Consumer { player: Player -> this.hide(player) })
    }

    private fun sendPlayerInfoPacket(action: EnumWrappers.PlayerInfoAction, receiver: Player) {
        val packet = WrapperPlayServerPlayerInfo()
        packet.action = action
        val data: MutableList<PlayerInfoData> = ArrayList()
        data.add(
            PlayerInfoData(
                gameProfile, 0, EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText(gameProfile.name)
            )
        )
        packet.data = data
        packet.sendPacket(receiver)
    }

    private fun sendNamedEntitySpawnPacket(receiver: Player) {
        val packet = WrapperPlayServerNamedEntitySpawn()
        packet.entityID = (entityID)
        packet.playerUUID = (gameProfile.uuid)
        packet.position = (location!!.toVector())
        packet.yaw = (location!!.yaw)
        packet.pitch = (location!!.pitch)
        packet.sendPacket(receiver)
    }

    private fun sendEntityHeadRotation(receiver: Player) {
        val packet = WrapperPlayServerEntityHeadRotation()
        packet.entityID = (entityID)
        packet.headYaw = location!!.yaw.toInt().toByte()
        packet.sendPacket(receiver)
    }

    private fun sendEntityMetadataPacket(index: Int, value: Any, receiver: Player) {
        val packet = WrapperPlayServerEntityMetadata()
        packet.entityID = (entityID)
        val watcher = WrappedDataWatcher()
        val `object`: WrappedDataWatcher.WrappedDataWatcherObject = WrappedDataWatcher.WrappedDataWatcherObject(
            index,
            WrappedDataWatcher.Registry.get(value.javaClass)
        )
        watcher.setObject(`object`, value)
        packet.metadata = (watcher.watchableObjects)
        packet.sendPacket(receiver)
    }

    private fun sendScoreboardTeamPacket(player: Player) {
        var packet = WrapperPlayServerScoreboardTeam()
        packet.name = ("npc-$entityID")
        packet.displayName = (WrappedChatComponent.fromText(""))
        packet.mode = (WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED)
        packet.nameTagVisibility = (if (hideNameTag) "never" else "always")
        packet.collisionRule = (if (collision) "always" else "never")
        packet.sendPacket(player)
        packet = WrapperPlayServerScoreboardTeam()
        packet.name = ("npc-$entityID")
        packet.mode = (WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED)
        packet.players = (listOf(gameProfile.name))
        packet.sendPacket(player)
    }

    private fun sendEntityDestroyPacket(receiver: Player) {
        val packet = WrapperPlayServerEntityDestroy()
        packet.setEntityIds(intArrayOf(entityID))
        packet.sendPacket(receiver)
    }

    private fun startListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(object : PacketAdapter(
            CringePvP.instance,
            WrapperPlayClientUseEntity.TYPE
        ) {
            override fun onPacketReceiving(event: PacketEvent) {
                val packet = WrapperPlayClientUseEntity(event.packet)
                if (interactList.containsKey(packet.targetID)) {
                    val previouslyClicked: Int = cache.getIfPresent(event.player.uniqueId) ?: return
                    if (previouslyClicked == packet.targetID) return
                    cache.put(event.player.uniqueId, packet.targetID)
                    Bukkit.getScheduler().runTask(CringePvP.instance,
                        Runnable {
                            interactList[packet.targetID]!!
                                .click(event.player, packet.type)
                        })
                }
            }
        })
    }

    class NPCBuilder {
        private val npc: SimpleNPC = SimpleNPC()

        fun build(): SimpleNPC {
            return npc
        }

        fun name(s: String?): NPCBuilder {
            val newProfile = WrappedGameProfile(npc.gameProfile.uuid, s)
            newProfile.properties.putAll(npc.gameProfile.properties)
            npc.gameProfile = newProfile
            return this
        }

        fun location(location: Location?): NPCBuilder {
            npc.location = location
            return this
        }

        fun interact(interact: Interact): NPCBuilder {
            interactList[npc.entityID] = interact
            return this
        }

        fun hideNameTag(b: Boolean): NPCBuilder {
            npc.hideNameTag = b
            return this
        }

        fun collision(b: Boolean): NPCBuilder {
            npc.collision = b
            return this
        }

        fun hideFromTabList(b: Boolean): NPCBuilder {
            npc.hideFromTabList = b
            return this
        }

        fun skin(texture: String?, signature: String?): NPCBuilder {
            npc.gameProfile.properties.put(
                "textures",
                WrappedSignedProperty("textures", texture, signature)
            )
            return this
        }

        fun skin(skin: NPCSkin): NPCBuilder {
            return this.skin(skin.texture, skin.signature)
        }

        fun skin(player: Player?): NPCBuilder {
            val skinData: WrappedSignedProperty = WrappedGameProfile.fromPlayer(player).properties
                .get("textures").stream().findAny().orElse(null) ?: throw RuntimeException("Missing skin data")
            return this.skin(skinData.value, skinData.signature)
        }
    }

    fun interface Interact {
        fun click(player: Player?, action: EnumWrappers.EntityUseAction?)
    }

    companion object {
        private var listenerRegistered = false
        private val interactList: MutableMap<Int, Interact> = HashMap()
        private val cache: Cache<UUID, Int> = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.SECONDS).build()

        fun builder(): NPCBuilder {
            return NPCBuilder()
        }
    }
}