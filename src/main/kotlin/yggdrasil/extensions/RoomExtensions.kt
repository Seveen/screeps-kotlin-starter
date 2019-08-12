package yggdrasil.extensions

import screeps.api.*
import screeps.api.structures.Structure

fun Room.findEnergyStructures() : List<Structure>{
    return find(FIND_MY_STRUCTURES)
            .filter {
                it.structureType == STRUCTURE_EXTENSION ||
                it.structureType == STRUCTURE_SPAWN ||
                it.structureType == STRUCTURE_TOWER ||
                it.structureType == STRUCTURE_CONTAINER
            }
}

fun Room.findNotFullEnergyStructures() : List<Structure>{
    return findEnergyStructures()
            .filter { it.unsafeCast<EnergyContainer>().energy < it.unsafeCast<EnergyContainer>().energyCapacity }
            .sortedBy { it.unsafeCast<EnergyContainer>().energy }
}

fun Room.findLowestStructureToRepair(): Structure? {
    return find(FIND_STRUCTURES, options {
        filter = {
            it.hits < it.hitsMax
        }
    }).minBy { it.hits }
}