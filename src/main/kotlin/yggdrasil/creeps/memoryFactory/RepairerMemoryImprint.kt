package yggdrasil.creeps.memoryFactory

import screeps.api.CreepMemory
import screeps.utils.unsafe.jsObject
import yggdrasil.Role
import yggdrasil.creeps.MemoryImprint

class RepairerMemoryImprint: MemoryImprint {
    override fun initMemory(): CreepMemory {
        return jsObject {
            this.repairing = false
            this.role = Role.REPAIRER
        }
    }
}