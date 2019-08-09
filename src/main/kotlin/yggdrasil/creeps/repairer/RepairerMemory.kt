package yggdrasil.creeps.repairer

import screeps.api.CreepMemory
import screeps.utils.memory.memory

class RepairerMemory: CreepMemory {
    var repairing: Boolean by memory { false }
}