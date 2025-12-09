package util

class UnionFind<T> {

    private val representatives = mutableMapOf<T, T>()

    tailrec fun findRepresentative(value: T): T {
        val representativeOfValue = representatives[value] ?: value

        return if (representativeOfValue == value) value else findRepresentative(representativeOfValue)
    }

    fun union(values: Pair<T, T>) {
        representatives.computeIfAbsent(values.first) { values.first }
        representatives.computeIfAbsent(values.second) { values.second }

        val firstRepresentative = findRepresentative(values.first)
        val secondRepresentative = findRepresentative(values.second)

        representatives[firstRepresentative] = secondRepresentative
    }

    fun unionAll(values: List<Pair<T, T>>) = values.forEach { union(it) }

    fun getGroups(values: Set<T>): List<List<T>> {
        return values
            .groupBy { findRepresentative(it) }
            .values
            .toList()
    }

}

fun <T> unionFindOf(pairs: List<Pair<T, T>>): UnionFind<T> =
    UnionFind<T>().also { it.unionAll(pairs) }
