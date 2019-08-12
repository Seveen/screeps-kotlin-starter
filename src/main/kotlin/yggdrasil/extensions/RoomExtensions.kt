package yggdrasil.extensions

import screeps.api.*
import screeps.api.structures.Structure

fun Room.findEnergyStructures() : List<Structure>{
    return find(FIND_MY_STRUCTURES)
            .filter {
                it.structureType == STRUCTURE_EXTENSION ||
                it.structureType == STRUCTURE_SPAWN ||
                it.structureType == STRUCTURE_TOWER ||
                it.structureType == STRUCTURE_STORAGE
            }
            .sortedWith(StructureEnergyContentComparator())
}

fun Room.findNotFullEnergyStructures() : List<Structure>{
    return findEnergyStructures()
            .filter { it.energyContent < it.energyContentCapacity }
            .sortedWith(StructureEnergyContentComparator())
}

fun Room.findNotFullEnergyStructuresByClosestFrom(target: HasPosition) : List<Structure>{
    return findEnergyStructures()
            .filter { it.energyContent < it.energyContentCapacity }
            .sortedWith(StructureEnergyContentComparator().then(DistanceComparator(target)))
}

fun Room.findLowestStructureToRepair(): Structure? {
    return find(FIND_STRUCTURES, options {
        filter = {
            it.hits < it.hitsMax
        }
    }).minBy { it.hits }
}
