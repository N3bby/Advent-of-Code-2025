package day8

import util.allValues

class UnionFind<T>(values: Set<T>) {

    val representatives = values
        .associateWith { it }
        .toMutableMap()

    tailrec fun findRepresentative(value: T): T {
        val representativeOfValue = representatives[value] ?: value

        return if (representativeOfValue == value) value else findRepresentative(representativeOfValue)
    }

    fun union(values: Pair<T, T>) {
        val firstRepresentative = findRepresentative(values.first)
        val secondRepresentative = findRepresentative(values.second)

        representatives[firstRepresentative] = secondRepresentative
    }

    fun unionAll(values: List<Pair<T, T>>) = values.forEach { union(it) }

    fun getGroups(values: Set<T>): Collection<List<T>> {
        return values
            .groupBy { findRepresentative(it) }
            .values
    }

    companion object {
        fun <T> fromPairs(pairs: List<Pair<T, T>>): UnionFind<T> {
            val unionFind = UnionFind(pairs.allValues())
            unionFind.unionAll(pairs)
            return unionFind
        }
    }

}
