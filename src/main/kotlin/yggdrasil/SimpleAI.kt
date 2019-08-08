package yggdrasil

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.utils.isEmpty
import screeps.utils.unsafe.delete
import screeps.utils.unsafe.jsObject
import yggdrasil.creeps.memoryFactory.BasicCreepMemoryImprint
import yggdrasil.creeps.MemoryImprint
import yggdrasil.creeps.upgrader.upgrade
import yggdrasil.creeps.builder.build
import yggdrasil.creeps.memoryFactory.BuilderMemoryImprint
import yggdrasil.creeps.memoryFactory.UpgraderMemoryImprint
import yggdrasil.creeps.memoryFactory.role

fun gameLoop() {
    val mainSpawn: StructureSpawn = Game.spawns.values.firstOrNull() ?: return

    //delete memories of creeps that have passed away
    houseKeeping(Game.creeps)

    //make sure we have at least some creeps
    spawnCreeps(Game.creeps.values, mainSpawn)

    // build a few extensions so we can have 550 energy
    val controller = mainSpawn.room.controller
    if (controller != null && controller.level >= 2) {
        val x = mainSpawn.pos.x
        val y = mainSpawn.pos.y
        when (controller.room.find(FIND_MY_STRUCTURES).count { it.structureType == STRUCTURE_EXTENSION }) {
            0 -> controller.room.createConstructionSite(x + 1, y + 1, STRUCTURE_EXTENSION)
            1 -> controller.room.createConstructionSite(x - 1, y - 1, STRUCTURE_EXTENSION)
            2 -> controller.room.createConstructionSite(x - 1, y + 1, STRUCTURE_EXTENSION)
            3 -> controller.room.createConstructionSite(x + 1, y - 1, STRUCTURE_EXTENSION)
            4 -> controller.room.createConstructionSite(x + 2, y - 2, STRUCTURE_EXTENSION)
            5 -> controller.room.createConstructionSite(x + 2, y - 2, STRUCTURE_EXTENSION)
            6 -> controller.room.createConstructionSite(x + 2, y - 2, STRUCTURE_EXTENSION)
        }
    }

    //spawn a big creep if we have plenty of energy
    for ((_, room) in Game.rooms) {
        if (room.energyAvailable >= 550) {
            mainSpawn.spawnCreep(
                    arrayOf(
                            WORK,
                            WORK,
                            WORK,
                            WORK,
                            CARRY,
                            MOVE,
                            MOVE
                    ),
                    "HarvesterBig_${Game.time}",
                    options {
                        memory = jsObject<CreepMemory> {
                            this.role = Role.HARVESTER
                        }
                    }
            )
            console.log("hurray!")
        }
    }

    for ((_, creep) in Game.creeps) {
        when (creep.memory.role) {
            Role.HARVESTER -> creep.harvest()
            Role.BUILDER -> creep.build()
            Role.UPGRADER -> creep.upgrade(mainSpawn.room.controller!!)
            else -> creep.pause()
        }
    }
}

private fun spawnCreeps(
        creeps: Array<Creep>,
        spawn: StructureSpawn
) {

    val body = arrayOf<BodyPartConstant>(WORK, CARRY, MOVE)

    if (spawn.room.energyAvailable < body.sumBy { BODYPART_COST[it]!! }) {
        return
    }

    val role: Role = when {
        creeps.count { it.memory.role == Role.HARVESTER } < 2 -> Role.HARVESTER

        creeps.none { it.memory.role == Role.UPGRADER } -> Role.UPGRADER

        spawn.room.find(FIND_MY_CONSTRUCTION_SITES).isNotEmpty() &&
                creeps.count { it.memory.role == Role.BUILDER } < 3 -> Role.BUILDER

        else -> return
    }

    val newName = "${role.name}_${Game.time}"
    val memoryImprint: MemoryImprint = when (role) {
        Role.UPGRADER -> UpgraderMemoryImprint()
        Role.BUILDER -> BuilderMemoryImprint()
        else -> BasicCreepMemoryImprint()
    }
    val code = spawn.spawnCreep(body, newName, options {
        memory = memoryImprint.initMemory()
    })

    when (code) {
        OK -> console.log("spawning $newName with body $body")
        ERR_BUSY, ERR_NOT_ENOUGH_ENERGY -> run { } // do nothing
        else -> console.log("unhandled error code $code")
    }
}

private fun houseKeeping(creeps: Record<String, Creep>) {
    if (Game.creeps.isEmpty()) return  // this is needed because Memory.creeps is undefined

    for ((creepName, _) in Memory.creeps) {
        if (creeps[creepName] == null) {
            console.log("deleting obsolete memory entry for creep $creepName")
            delete(Memory.creeps[creepName])
        }
    }
}
