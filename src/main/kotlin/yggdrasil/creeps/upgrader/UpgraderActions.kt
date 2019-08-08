package yggdrasil.creeps.upgrader

import screeps.api.Creep
import screeps.api.ERR_NOT_ENOUGH_ENERGY
import screeps.api.ERR_NOT_IN_RANGE
import screeps.api.FIND_SOURCES
import screeps.api.structures.StructureController
import yggdrasil.creeps.memoryFactory.upgrading

fun Creep.upgrade(controller: StructureController) {
    when(memory.upgrading) {
        true -> {
            when (upgradeController(controller)) {
                ERR_NOT_IN_RANGE -> moveTo(controller.pos)
                ERR_NOT_ENOUGH_ENERGY -> memory.upgrading = false
                else -> {}
            }
        }
        false -> {
            val sources = room.find(FIND_SOURCES)
            when (carry.energy == carryCapacity) {
                true -> memory.upgrading = true
                false -> when (harvest(sources[0])) {
                    ERR_NOT_IN_RANGE -> moveTo(sources[0].pos)
                    else -> {}
                }
            }
        }
    }
}