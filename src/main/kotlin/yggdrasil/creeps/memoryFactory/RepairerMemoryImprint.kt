package yggdrasil.creeps.memoryFactory

import screeps.api.CreepMemory
import screeps.utils.unsafe.jsObject
import yggdrasil.Role
import yggdrasil.creeps.MemoryImprint
import yggdrasil.creeps.repairer.RepairerMemory

class RepairerMemoryImprint: MemoryImprint {
    override fun initMemory(): RepairerMemory {
        return jsObject {
            this.repairing = false
            this.role = Role.REPAIRER
        }
    }
}