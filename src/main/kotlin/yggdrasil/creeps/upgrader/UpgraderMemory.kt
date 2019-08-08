package yggdrasil.creeps.upgrader

import screeps.api.CreepMemory
import screeps.utils.memory.memory

class UpgraderMemory : CreepMemory {
    var upgrading: Boolean by memory { false }
}
