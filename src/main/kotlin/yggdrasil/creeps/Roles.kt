package yggdrasil

import screeps.api.*
import screeps.api.structures.Structure
import yggdrasil.creeps.memoryFactory.pause
import yggdrasil.creeps.memoryFactory.role
import yggdrasil.extensions.ResourceAmountComparator
import yggdrasil.extensions.findNotFullEnergyStructures
import kotlin.random.Random


enum class Role {
    UNASSIGNED,
    HARVESTER,
    MINER,
    RUNNER,
    BUILDER,
    UPGRADER,
    REPAIRER
}

fun Creep.pause() {
    if (memory.pause < 10) {
        //blink slowly
        if (memory.pause % 3 != 0) say("\uD83D\uDEAC")
        memory.pause++
    } else {
        memory.pause = 0
        memory.role = Role.HARVESTER
    }
}

fun Creep.harvest(fromRoom: Room = this.room, toRoom: Room = this.room) {
    if (carry.energy < carryCapacity) {
        val onTheGround = toRoom.find(FIND_DROPPED_RESOURCES, options {
            filter = {
                it.resourceType == RESOURCE_ENERGY
            }
        }).sortedWith(ResourceAmountComparator())
        if (onTheGround.isNotEmpty()) {
            if (pickup(onTheGround[0]) == ERR_NOT_IN_RANGE) {
                moveTo(onTheGround[0].pos)
            }
        } else {
            val sources = toRoom.find(FIND_SOURCES)
            if (harvest(sources[1]) == ERR_NOT_IN_RANGE) {
                moveTo(sources[1].pos)
            }
        }
    } else {
        val targets = fromRoom.findNotFullEnergyStructures()

        if (targets.isNotEmpty()) {
            if (transfer(targets[0], RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                moveTo(targets[0].pos)
            }
        }
    }
}
