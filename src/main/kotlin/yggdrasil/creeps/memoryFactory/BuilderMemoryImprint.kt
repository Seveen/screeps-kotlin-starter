package yggdrasil.creeps.memoryFactory

import screeps.utils.unsafe.jsObject
import yggdrasil.Role
import yggdrasil.creeps.MemoryImprint
import yggdrasil.creeps.builder.BuilderMemory

class BuilderMemoryImprint : MemoryImprint {
    override fun initMemory(): BuilderMemory {
        return jsObject {
            this.building = false
            this.role = Role.BUILDER
        }
    }
}