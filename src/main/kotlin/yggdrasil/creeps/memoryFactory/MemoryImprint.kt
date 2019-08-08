package yggdrasil.creeps

import screeps.api.CreepMemory

interface MemoryImprint {
    fun initMemory(): CreepMemory
}