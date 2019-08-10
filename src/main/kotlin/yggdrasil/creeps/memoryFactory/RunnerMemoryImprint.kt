package yggdrasil.creeps.memoryFactory

import screeps.api.CreepMemory
import screeps.utils.unsafe.jsObject
import yggdrasil.Role
import yggdrasil.creeps.MemoryImprint
import yggdrasil.creeps.runner.RunnerMemory

class RunnerMemoryImprint(private val source: String = ""): MemoryImprint {
    override fun initMemory(): RunnerMemory {
        return jsObject {
            this.running = false
            this.role = Role.RUNNER
            this.assignedSource = source
        }
    }
}