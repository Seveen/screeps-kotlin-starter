package yggdrasil.creeps.runner

import screeps.api.*
import screeps.api.structures.Structure
import yggdrasil.Role
import yggdrasil.creeps.memoryFactory.assignedSource
import yggdrasil.creeps.memoryFactory.role
import yggdrasil.creeps.memoryFactory.running

fun Creep.run(assignedRoom: Room = this.room) {
    if (memory.assignedSource == "") {
        memory.assignedSource = assignedRoom.find(FIND_STRUCTURES, options {
            filter = {
                it.structureType == STRUCTURE_CONTAINER
            }
        }).filter {foundSource ->
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
        }[0].id
    }

    val source = Game.getObjectById<Structure>(memory.assignedSource)
    val storage = assignedRoom.find(FIND_MY_STRUCTURES)
            .filter { (it.structureType == STRUCTURE_EXTENSION || it.structureType == STRUCTURE_SPAWN) }
            .filter { it.unsafeCast<EnergyContainer>().energy < it.unsafeCast<EnergyContainer>().energyCapacity }
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
}