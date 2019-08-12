package yggdrasil.extensions

import screeps.api.Creep
import screeps.api.structures.Structure

fun Creep.gtfo(structure: Structure) {
    val deltaX = pos.x - structure.pos.x
    val deltaY = pos.y - structure.pos.y

    if (deltaX in -1..1 && deltaY in -1..1) {
        moveTo(pos.x + deltaX, pos.y + deltaY)
    }
}