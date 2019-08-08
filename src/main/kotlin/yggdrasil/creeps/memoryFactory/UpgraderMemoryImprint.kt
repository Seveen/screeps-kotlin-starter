package yggdrasil.creeps.memoryFactory

import screeps.utils.unsafe.jsObject
import yggdrasil.Role
import yggdrasil.creeps.MemoryImprint
import yggdrasil.creeps.upgrader.UpgraderMemory

class UpgraderMemoryImprint: MemoryImprint {
    override fun initMemory() : UpgraderMemory {
        return jsObject {
            this.upgrading = false
            this.role = Role.UPGRADER
        }
    }
}