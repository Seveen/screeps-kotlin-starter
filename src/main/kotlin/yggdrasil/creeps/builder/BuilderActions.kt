package yggdrasil.creeps.builder

import screeps.api.*
import yggdrasil.creeps.memoryFactory.building

//TODO: rewrite cleanly
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
        val storage = assignedRoom.find(FIND_MY_STRUCTURES)
            .filter { (it.structureType == STRUCTURE_EXTENSION || it.structureType == STRUCTURE_SPAWN) }
            .filter { it.unsafeCast<EnergyContainer>().energy < it.unsafeCast<EnergyContainer>().energyCapacity }

        storage[0].let {
            when (withdraw(it, RESOURCE_ENERGY)) {
                ERR_NOT_IN_RANGE -> moveTo(it)
                OK -> {
                    val deltaX = when  {
                        (it.pos.x - pos.x) > 0 -> 1
                        (it.pos.x - pos.x) < 0 -> -1
                        else -> 0
                    }
                    val deltaY = when  {
                        (it.pos.y - pos.y) > 0 -> 1
                        (it.pos.y - pos.y) < 0 -> -1
                        else -> 0
                    }
                    moveTo(pos.x - deltaX, pos.y - deltaY)
                }
                else -> {}
            }
        }
    }

}