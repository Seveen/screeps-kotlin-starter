package yggdrasil.creeps.builder

import screeps.api.*
import yggdrasil.creeps.memoryFactory.building

fun Creep.build(assignedRoom: Room = this.room) {
    if (memory.building && carry.energy == 0) {
        memory.building = false
        say("🔄 harvest")
    }
    if (!memory.building && carry.energy == carryCapacity) {
        memory.building = true
        say("🚧 build")
    }

    if (memory.building) {
        val targets = assignedRoom.find(FIND_MY_CONSTRUCTION_SITES)
        if (targets.isNotEmpty()) {
            targets.sort { site1, site2 ->
                val range1 = site1.pos.getRangeTo(this)
                val range2 = site2.pos.getRangeTo(this)

                when {
                    range1 > range2 -> 1
                    range1 < range2 -> -1
                    else -> 0
                }
            }

            if (build(targets[0]) == ERR_NOT_IN_RANGE) {
                moveTo(targets[0].pos)
            }
        }
    } else {
        val sources = room.find(FIND_SOURCES)
        if (harvest(sources[1]) == ERR_NOT_IN_RANGE) {
            moveTo(sources[1].pos)
        }
    }

}