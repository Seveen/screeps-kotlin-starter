package yggdrasil.creeps.memoryFactory

import screeps.api.CreepMemory
import screeps.utils.memory.memory
import screeps.utils.unsafe.jsObject
import yggdrasil.Role
import yggdrasil.creeps.MemoryImprint

class BasicCreepMemory() : CreepMemory {}

class BasicCreepMemoryImprint : MemoryImprint {
    override fun initMemory(): CreepMemory {
        return jsObject {
            this.role = Role.HARVESTER
        }
    }
}

var CreepMemory.role by memory(Role.UNASSIGNED)
var CreepMemory.pause: Int by memory { 0 }
var CreepMemory.upgrading: Boolean by memory { false }
var CreepMemory.building: Boolean by memory { false }
var CreepMemory.repairing: Boolean by memory { false }
var CreepMemory.running: Boolean by memory { false }
var CreepMemory.mining: Boolean by memory { false }
var CreepMemory.assignedSource: String by memory { "" }

