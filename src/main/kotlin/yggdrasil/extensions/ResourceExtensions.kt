package yggdrasil.extensions

import screeps.api.Resource

class ResourceAmountComparator : Comparator<Resource> {
    override fun compare(a: Resource, b: Resource): Int {
        return when {
            a.amount < b.amount -> -1
            a.amount > b.amount -> 1
            else -> 0
        }
    }
}