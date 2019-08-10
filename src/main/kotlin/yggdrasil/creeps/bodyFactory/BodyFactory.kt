package yggdrasil.creeps.bodyFactory

import screeps.api.BodyPartConstant
import screeps.api.CARRY
import screeps.api.WORK
import screeps.api.MOVE
import yggdrasil.Role

object BodyFactory {

    private val UpgraderBody = arrayOf<BodyPartConstant>(WORK ,WORK, CARRY, CARRY, CARRY, MOVE, MOVE, MOVE)
    private val RepairerBody = arrayOf<BodyPartConstant>(WORK ,WORK, CARRY, CARRY, CARRY, MOVE, MOVE, MOVE)
    private val BuilderBody = arrayOf<BodyPartConstant>(WORK ,WORK, CARRY, CARRY, CARRY, MOVE, MOVE, MOVE)
    private val HarvesterBody = arrayOf<BodyPartConstant>(WORK, CARRY, MOVE)
    private val MinerBody = arrayOf<BodyPartConstant>(WORK, WORK, WORK, CARRY, MOVE)
    private val RunnerBody = arrayOf<BodyPartConstant>(CARRY, CARRY, CARRY, CARRY, MOVE, MOVE, MOVE, MOVE)
    private val BasicBody = arrayOf<BodyPartConstant>(WORK, CARRY, MOVE)

    fun getBody(role: Role): Array<BodyPartConstant> {
        return when (role) {
            Role.UPGRADER -> UpgraderBody
            Role.REPAIRER -> RepairerBody
            Role.BUILDER -> BuilderBody
            Role.HARVESTER -> HarvesterBody
            Role.MINER -> MinerBody
            Role.RUNNER -> RunnerBody
            Role.UNASSIGNED -> BasicBody
        }
    }
}