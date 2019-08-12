package yggdrasil.extensions

import screeps.api.*
import screeps.api.structures.Structure

val Structure.energyContent: Int
    get() = when (structureType) {
        STRUCTURE_SPAWN -> unsafeCast<EnergyContainer>().energy
        STRUCTURE_EXTENSION -> unsafeCast<EnergyContainer>().energy
        STRUCTURE_TOWER -> unsafeCast<EnergyContainer>().energy
        STRUCTURE_STORAGE -> unsafeCast<Store>().store.energy
        else -> 0
    }

val Structure.energyContentCapacity: Int
    get() = when(structureType) {
        STRUCTURE_SPAWN -> unsafeCast<EnergyContainer>().energyCapacity
        STRUCTURE_EXTENSION -> unsafeCast<EnergyContainer>().energyCapacity
        STRUCTURE_TOWER -> unsafeCast<EnergyContainer>().energyCapacity
        STRUCTURE_STORAGE -> unsafeCast<Store>().storeCapacity
        else -> 0
    }

class StructureEnergyContentComparator : Comparator<Structure> {
    override fun compare(a: Structure, b: Structure): Int {
        val energyContentA = when (a.structureType) {
            STRUCTURE_SPAWN -> a.unsafeCast<EnergyContainer>().energy
            STRUCTURE_EXTENSION -> a.unsafeCast<EnergyContainer>().energy
            STRUCTURE_TOWER -> a.unsafeCast<EnergyContainer>().energy
            STRUCTURE_STORAGE -> a.unsafeCast<Store>().store.energy
            else -> 0
        }

        val energyContentB = when (b.structureType) {
            STRUCTURE_SPAWN -> b.unsafeCast<EnergyContainer>().energy
            STRUCTURE_EXTENSION -> b.unsafeCast<EnergyContainer>().energy
            STRUCTURE_TOWER -> b.unsafeCast<EnergyContainer>().energy
            STRUCTURE_STORAGE -> b.unsafeCast<Store>().store.energy
            else -> 0
        }

        return when {
            energyContentA < energyContentB -> -1
            energyContentA > energyContentB -> 1
            else -> 0
        }
    }
}