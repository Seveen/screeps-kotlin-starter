package yggdrasil.extensions

import screeps.api.*
import screeps.api.structures.Structure

fun Room.findEnergyStructures() : List<Structure>{
    return find(FIND_MY_STRUCTURES)
            .filter { (it.structureType == STRUCTURE_EXTENSION || it.structureType == STRUCTURE_SPAWN) }
}

fun Room.findNotFullEnergyStructures() : List<Structure>{
    return find(FIND_MY_STRUCTURES)
            .filter { (it.structureType == STRUCTURE_EXTENSION || it.structureType == STRUCTURE_SPAWN) }
            .filter { it.unsafeCast<EnergyContainer>().energy < it.unsafeCast<EnergyContainer>().energyCapacity }
}