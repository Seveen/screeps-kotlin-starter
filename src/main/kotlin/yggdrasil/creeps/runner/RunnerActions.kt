package yggdrasil.creeps.runner

import screeps.api.*
import screeps.api.structures.Structure
import yggdrasil.Role
import yggdrasil.creeps.memoryFactory.assignedSource
import yggdrasil.creeps.memoryFactory.role
import yggdrasil.creeps.memoryFactory.running

fun Creep.run(assignedRoom: Room = this.room) {
    if (memory.assignedSource == "") {
        memory.assignedSource = assignedRoom.find(FIND_MY_STRUCTURES, options {
            filter = {
                it.structureType == STRUCTURE_CONTAINER
            }
        }).filter {foundSource ->
            var result = true
            Game.creeps.values.filter {
                it.memory.role == Role.RUNNER
            }
            .forEach {
                if (it.memory.assignedSource == foundSource.id) {
                    result = false
                }

            }
            result
        }[0].id
    }

    val source = Game.getObjectById<Structure>(memory.assignedSource)
    val storage = assignedRoom.storage!!
    source?.let { actualSource ->
        when (memory.running) {
            true -> {
                when(withdraw(actualSource, RESOURCE_ENERGY)) {
                    ERR_NOT_IN_RANGE -> moveTo(actualSource)
                    OK -> memory.running = false
                    else -> {}
                }
            }
            false -> {
                when(transfer(storage, RESOURCE_ENERGY)) {
                    ERR_NOT_IN_RANGE -> moveTo(storage)
                    OK -> memory.running = true
                    else -> {}
                }
            }
        }
    }
}