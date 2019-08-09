package yggdrasil.creeps.miner

import screeps.api.*
import yggdrasil.Role
import yggdrasil.creeps.memoryFactory.assignedSource
import yggdrasil.creeps.memoryFactory.mining
import yggdrasil.creeps.memoryFactory.role

fun Creep.mine(assignedRoom: Room = this.room) {
    if (memory.assignedSource == "") {
        memory.assignedSource = assignedRoom.find(FIND_SOURCES).filter { foundSource ->
            var result = true
            Game.creeps.values.filter {
                it.memory.role == Role.MINER
            }
                    .forEach {
                        if (it.memory.assignedSource == foundSource.id) {
                            result = false
                        }

                    }
            result
        }[0].id
    }

    val source = Game.getObjectById<Source>(memory.assignedSource)
    val storage = assignedRoom.find(FIND_MY_STRUCTURES, options {
        filter = {
            it.structureType == STRUCTURE_CONTAINER
        }
    }).also{
        it.sort { a, b ->
            val rangeA = this.pos.getRangeTo(a.pos)
            val rangeB = this.pos.getRangeTo(b.pos)

            when {
                rangeA < rangeB -> 1
                rangeA > rangeB -> -1
                else -> 0
            }
        }
    }[0]

    source?.let { actualSource ->
        when (memory.mining) {
            true -> {
                when (harvest(actualSource)) {
                    ERR_NOT_IN_RANGE -> moveTo(actualSource)
                    else -> if (carry.energy == carryCapacity) {
                        memory.mining = false
                    } else {}
                }
            }
            false -> {
                when (transfer(storage, RESOURCE_ENERGY)) {
                    ERR_NOT_IN_RANGE -> moveTo(storage)
                    OK -> memory.mining = true
                    else -> {}
                }
            }
        }
    }
}