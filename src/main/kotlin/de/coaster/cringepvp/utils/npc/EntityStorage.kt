package de.coaster.cringepvp.utils.npc

import de.coaster.cringepvp.utils.FileConfig
import de.coaster.cringepvp.utils.npc.wrappers.NPCSkin
import java.io.File

object EntityStorage {

    // The list of all NPCs
    private val entities = mutableMapOf<Int, SimpleNPC>()

    init {
        load()
    }

    // Load NPCs from FileStorage
    private fun load() {
        val dir = File("config/npcs")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        for (file in dir.listFiles()!!) {
            val npcConfig = FileConfig(file.absolutePath)
            val id = npcConfig.getInt("id")
            val name = npcConfig.getString("name")
            val location = npcConfig.getLocation("location")
            val skin = npcConfig.getString("skin", null)?.let { NPCSkin.fromString(it) }
            val collision = npcConfig.getBoolean("collision", true)
            val hideFromTabList = npcConfig.getBoolean("hideFromTabList", true)
            val hideNameTag = npcConfig.getBoolean("hideNameTag", false)

            val npc = SimpleNPC.NPCBuilder(id)
                .collision(collision)
                .location(location)
                .name(name)
                .hideFromTabList(hideFromTabList)
                .hideNameTag(hideNameTag)

            skin?.let { npc.skin(it) }
            entities[id] = npc.build()
        }
    }

    // Save NPCs to FileStorage
    fun save() {
        for (npc in entities.values) {
            save(npc)
        }
    }

    private fun save(npc: SimpleNPC) {
        val npcConfig = FileConfig("config/npcs/${npc.entityID}.yml")
        npcConfig.set("id", npc.entityID)
        npcConfig.set("name", npc.gameProfile.name)
        npcConfig.set("location", npc.location)
        npcConfig.set("skin", npc.getSkinAsString())
        npcConfig.set("collision", npc.collision)
        npcConfig.set("hideFromTabList", npc.hideFromTabList)
        npcConfig.set("hideNameTag", npc.hideNameTag)
        npcConfig.saveConfig()
    }
}