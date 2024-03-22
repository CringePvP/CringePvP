package de.coaster.cringepvp.utils.npc.wrappers

data class NPCSkin(val texture: String, val signature: String) {
    companion object {
        fun fromString(base64: String): NPCSkin {
            val split = base64.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return NPCSkin(split[0], split[1])
        }
    }

    override fun toString(): String {
        return "$texture:$signature"
    }
}