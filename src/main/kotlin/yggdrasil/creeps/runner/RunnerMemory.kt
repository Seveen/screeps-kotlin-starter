package yggdrasil.creeps.runner

import screeps.api.CreepMemory
import screeps.utils.memory.memory

class RunnerMemory : CreepMemory {
    var assignedSource: String by memory { "" }
    var running: Boolean by memory { false }
}