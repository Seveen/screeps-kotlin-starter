package yggdrasil.creeps.repairer

import screeps.api.*
import screeps.api.structures.Structure
import yggdrasil.creeps.memoryFactory.repairing


fun Creep.maintain(assignedRoom: Room = this.room) {
    when(memory.repairing) {
        true -> {
            val target = findLowestStructureToRepair(assignedRoom)
            target?.let {
                when (this.repair(target)) {
                    ERR_NOT_IN_RANGE -> moveTo(target)
                    ERR_NOT_ENOUGH_ENERGY -> memory.repairing = false
                    else -> {}
                }
            }
        }
        false -> {
            val sources = room.find(FIND_SOURCES)
            when (carry.energy == carryCapacity) {
                true -> memory.repairing = true
                false -> when (harvest(sources[0])) {
                    ERR_NOT_IN_RANGE -> moveTo(sources[0].pos)
                    else -> {}
                }
            }
        }
    }
}

fun findLowestStructureToRepair(room: Room): Structure? {
    val targets = room.find(FIND_STRUCTURES, options { filter = {
        it.hits < it.hitsMax
    }})

    targets.sort { one, two ->
        when {
            one.hits < two.hits -> -1
            one.hits > two.hits -> 1
            else -> 0
        }
    }

    return targets[0]
}