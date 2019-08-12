package yggdrasil.creeps.builder

import screeps.api.*
import yggdrasil.creeps.memoryFactory.building
import yggdrasil.extensions.findEnergyStructures
import yggdrasil.extensions.gtfo

fun Creep.build(assignedRoom: Room = this.room) {

    when(memory.building) {
        true -> {
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

                when (build(targets[0])) {
                    ERR_NOT_IN_RANGE -> moveTo(targets[0].pos)
                    ERR_NOT_ENOUGH_RESOURCES -> memory.building = false
                    else -> {
                    }
                }
            }
        }
        false -> {
            val storage = assignedRoom.findEnergyStructures()

            storage.firstOrNull()?.let {
                if (assignedRoom.energyAvailable >= 400) {
                    when (withdraw(it, RESOURCE_ENERGY)) {
                        ERR_NOT_IN_RANGE -> moveTo(it)
                        ERR_NOT_ENOUGH_RESOURCES -> gtfo(it)
                        else -> if (carry.energy == carryCapacity) {
                            memory.building = true
                        } else {}
                    }
                } else {
                    gtfo(it)
                    if (carry.energy == carryCapacity) {
                        memory.building = true
                    } else {}
                }
            }
        }
    }
}