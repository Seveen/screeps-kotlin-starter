package yggdrasil.creeps.memoryFactory

import screeps.api.CreepMemory
import screeps.utils.unsafe.jsObject
import yggdrasil.Role
import yggdrasil.creeps.MemoryImprint

class MinerMemoryImprint: MemoryImprint {
    override fun initMemory(): CreepMemory {
        return jsObject {
            this.role = Role.MINER
            this.assignedSource = ""
            this.mining = false
        }
    }
}