package yggdrasil.creeps.miner

import screeps.api.CreepMemory
import screeps.utils.memory.memory

class MinerMemory : CreepMemory {
    var assignedSource: String by memory { "" }
    var mining: Boolean by memory { false }
}