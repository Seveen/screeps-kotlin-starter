package yggdrasil.extensions

import screeps.api.HasPosition

class DistanceComparator(private val fromTarget: HasPosition): Comparator<HasPosition> {
    override fun compare(a: HasPosition, b: HasPosition): Int {
        val rangeToA = fromTarget.pos.getRangeTo(a.pos)
        val rangeToB = fromTarget.pos.getRangeTo(b.pos)
        return when {
            rangeToA < rangeToB -> -1
            rangeToA > rangeToB -> 1
            else -> 0
        }
    }
}