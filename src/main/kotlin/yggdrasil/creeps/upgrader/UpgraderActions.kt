package yggdrasil.creeps.upgrader

import screeps.api.*
import screeps.api.structures.StructureController
import yggdrasil.creeps.memoryFactory.building
import yggdrasil.creeps.memoryFactory.upgrading
import yggdrasil.extensions.findEnergyStructures
import yggdrasil.gtfo

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
            val storage = room.findEnergyStructures()

            storage.firstOrNull()?.let {
                if (room.energyAvailable >= 400) {
                    when (withdraw(it, RESOURCE_ENERGY)) {
                        ERR_NOT_IN_RANGE -> moveTo(it)
                        ERR_NOT_ENOUGH_RESOURCES -> gtfo(it)
                        else -> if (carry.energy == carryCapacity) {
                            memory.upgrading = true
                        } else {}
                    }
                } else {
                    gtfo(it)
                    if (carry.energy == carryCapacity) {
                        memory.upgrading = true
                    } else {}
                }
            }
        }
    }
}