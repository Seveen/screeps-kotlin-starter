package yggdrasil.creeps.builder

import screeps.api.CreepMemory
import screeps.utils.memory.memory

class BuilderMemory : CreepMemory {
    var building: Boolean by memory { false }
}

