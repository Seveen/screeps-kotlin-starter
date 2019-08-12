package yggdrasil.creeps.runner

import screeps.api.*
import screeps.api.structures.Structure
import yggdrasil.Role
import yggdrasil.creeps.memoryFactory.assignedSource
import yggdrasil.creeps.memoryFactory.role
import yggdrasil.creeps.memoryFactory.running
import yggdrasil.extensions.ResourceAmountComparator
import yggdrasil.extensions.findNotFullEnergyStructuresByClosestFrom

fun Creep.run(assignedRoom: Room = this.room) {
    if (memory.assignedSource == "") {
        assignedRoom.find(FIND_STRUCTURES, options {
            filter = {
                it.structureType == STRUCTURE_CONTAINER
            }
        }).firstOrNull { foundSource ->
            var result = true
            var numberOfCreepsOnSource = 0
            Game.creeps.values.filter {
                it.memory.role == Role.RUNNER
            }
            .forEach {
                if (it.memory.assignedSource == foundSource.id) {
                    numberOfCreepsOnSource++
                }
            }
            if (numberOfCreepsOnSource >= 2) {
                result = false
            }
            result
        }?.id?.let {
            memory.assignedSource = it
        }
    }

    val source = Game.getObjectById<Structure>(memory.assignedSource)
    val storage = assignedRoom.findNotFullEnergyStructuresByClosestFrom(this)

    source?.let { actualSource ->
        storage.firstOrNull()?.let {actualStorage ->
            when (memory.running) {
                true -> {
                    when(withdraw(actualSource, RESOURCE_ENERGY)) {
                        ERR_NOT_IN_RANGE -> moveTo(actualSource)
                        else -> if (carry.energy == carryCapacity) {
                            memory.running = false
                        } else {}
                    }
                }
                false -> {
                    when(transfer(actualStorage, RESOURCE_ENERGY)) {
                        ERR_NOT_IN_RANGE -> moveTo(actualStorage)
                        ERR_NOT_ENOUGH_RESOURCES -> memory.running = true
                        else -> {}
                    }
                }
            }
        }
    }
    if (source == null) {
        storage.firstOrNull()?.let {actualStorage ->
            when (memory.running) {
                true -> {
                    val onTheGround = assignedRoom.find(FIND_DROPPED_RESOURCES, options {
                        filter = {
                            it.resourceType == screeps.api.RESOURCE_ENERGY
                        }
                    }).sortedWith(ResourceAmountComparator())
                    if (onTheGround.isNotEmpty()) {
                        if (pickup(onTheGround[0]) == ERR_NOT_IN_RANGE) {
                            moveTo(onTheGround[0].pos)
                        } else {}
                    } else {}
                }
                false -> {
                    when(transfer(actualStorage, RESOURCE_ENERGY)) {
                        ERR_NOT_IN_RANGE -> moveTo(actualStorage)
                        ERR_NOT_ENOUGH_RESOURCES -> memory.running = true
                        else -> {}
                    }
                }
            }
        }
    }
}