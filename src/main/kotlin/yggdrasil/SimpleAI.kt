package yggdrasil

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.api.structures.StructureTower
import screeps.utils.isEmpty
import screeps.utils.unsafe.delete
import screeps.utils.unsafe.jsObject
import yggdrasil.creeps.MemoryImprint
import yggdrasil.creeps.bodyFactory.BodyFactory
import yggdrasil.creeps.upgrader.upgrade
import yggdrasil.creeps.builder.build
import yggdrasil.creeps.memoryFactory.*
import yggdrasil.creeps.miner.mine
import yggdrasil.creeps.repairer.maintain
import yggdrasil.creeps.runner.run
import yggdrasil.extensions.findLowestStructureToRepair

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
            5 -> controller.room.createConstructionSite(x + 2, y + 2, STRUCTURE_EXTENSION)
            6 -> controller.room.createConstructionSite(x - 2, y - 2, STRUCTURE_EXTENSION)
            7 -> controller.room.createConstructionSite(x - 2, y + 2, STRUCTURE_EXTENSION)
        }
    }

    for ((_, creep) in Game.creeps) {
        when (creep.memory.role) {
            Role.HARVESTER -> creep.harvest()
            Role.BUILDER -> creep.build()
            Role.UPGRADER -> creep.upgrade(mainSpawn.room.controller!!)
            Role.REPAIRER -> creep.maintain()
            Role.RUNNER -> creep.run()
            Role.MINER -> creep.mine()
            else -> creep.pause()
        }
    }

    for ((_, structure) in Game.structures) {
        when (structure.structureType) {
            STRUCTURE_TOWER -> {
                val repairTarget = structure.room.findLowestStructureToRepair()
                repairTarget?.let {
                    (structure as StructureTower).repair(it)
                }
            }
            else -> {}
        }
    }
}

private fun spawnCreeps(
        creeps: Array<Creep>,
        spawn: StructureSpawn
) {

    val numberOfStorages = spawn.room.find(FIND_STRUCTURES, options {
        filter = {it.structureType == STRUCTURE_CONTAINER}
    }).size

    val role: Role = when {
        creeps.isEmpty() -> Role.HARVESTER
        creeps.count { it.memory.role == Role.HARVESTER } < 2 -> Role.HARVESTER

        creeps.count { it.memory.role == Role.MINER } == 0 -> Role.MINER
        creeps.count { it.memory.role == Role.RUNNER } < numberOfStorages -> Role.RUNNER

        spawn.room.find(FIND_MY_CONSTRUCTION_SITES).isNotEmpty() &&
                creeps.count { it.memory.role == Role.BUILDER } == 0 -> Role.BUILDER

        creeps.count { it.memory.role == Role.RUNNER } < numberOfStorages*2 -> Role.RUNNER
        creeps.count { it.memory.role == Role.MINER } < numberOfStorages*2 -> Role.MINER

        creeps.count { it.memory.role == Role.UPGRADER } < 9 -> Role.UPGRADER

        creeps.count { it.memory.role == Role.REPAIRER } < 1 -> Role.REPAIRER


        spawn.room.find(FIND_MY_CONSTRUCTION_SITES).isNotEmpty() &&
                creeps.count { it.memory.role == Role.BUILDER } < 3 -> Role.BUILDER

        creeps.count { it.memory.role == Role.REPAIRER } < 3 &&
            spawn.room.find(FIND_STRUCTURES).count { it.structureType == STRUCTURE_TOWER } == 0 -> Role.REPAIRER

        else -> return
    }

    val body = BodyFactory.getBody(role)

    if (spawn.room.energyAvailable < body.sumBy { BODYPART_COST[it]!! }) {
        return
    }

    val newName = "${role.name}_${Game.time}"
    val memoryImprint: MemoryImprint = when (role) {
        Role.UPGRADER -> UpgraderMemoryImprint()
        Role.BUILDER -> BuilderMemoryImprint()
        Role.REPAIRER -> RepairerMemoryImprint()
        Role.RUNNER -> RunnerMemoryImprint()
        Role.MINER -> MinerMemoryImprint()
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
