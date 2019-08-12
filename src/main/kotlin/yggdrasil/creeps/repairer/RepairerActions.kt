package yggdrasil.creeps.repairer

import screeps.api.*
import screeps.api.structures.Structure
import yggdrasil.creeps.memoryFactory.repairing
import yggdrasil.creeps.memoryFactory.upgrading
import yggdrasil.extensions.findEnergyStructures
import yggdrasil.extensions.findLowestStructureToRepair
import yggdrasil.gtfo


fun Creep.maintain(assignedRoom: Room = this.room) {
    when(memory.repairing) {
        true -> {
            val target = assignedRoom.findLowestStructureToRepair()
            target?.let {
                when (this.repair(target)) {
                    ERR_NOT_IN_RANGE -> moveTo(target)
                    ERR_NOT_ENOUGH_ENERGY -> memory.repairing = false
                    else -> {}
                }
            }
        }
        false -> {
            val sources = room.findEnergyStructures()
            when (carry.energy == carryCapacity) {
                true -> memory.repairing = true
                false -> when (withdraw(sources[0], RESOURCE_ENERGY)) {
                    ERR_NOT_IN_RANGE -> moveTo(sources[0].pos)
                    ERR_NOT_ENOUGH_RESOURCES -> gtfo(sources[0])
                    else -> {}
                }
            }
            if (carry.energy == carryCapacity) {
                memory.repairing = true
            } else {}
        }
    }
}

