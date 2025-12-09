package util

import ext.swap

fun <T> cartesianProduct(list1: List<T>, list2: List<T>): List<Pair<T, T>> {
    val result = ArrayList<Pair<T, T>>(list1.size * list2.size)
    for (a in list1) {
        for (b in list2) {
            result.add(a to b)
        }
    }
    return result
}

fun <T> List<Pair<T, T>>.removeDuplicates(): List<Pair<T, T>> {
    val result = HashSet<Pair<T, T>>(size)
    for (pair in this) {
        if(pair.first == pair.second) continue
        if (pair !in result && pair.swap() !in result) result.add(pair)
    }
    return result.toList()
}

fun <T> List<Pair<T, T>>.allValues(): Set<T> =
    flatMap { (first, second) -> listOf(first, second) }.toSet()

fun integerSequence(): Sequence<Int> {
    return generateSequence(0) { it + 1 }
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

/** Swaps the elements from range1 with range2. If range2 contains more elements, they are ignored */
fun <T> MutableList<T>.swap(range1: IntRange, range2: IntRange) {
    range1.zip(range2).forEach {
        val temp = this[it.first]
        this[it.first] = this[it.second]
        this[it.second] = temp
    }
}

fun <T> List<Set<T>>.combine(): Set<T> {
    return this.fold(setOf()) { acc, set -> acc + set }
}

fun <T> List<T>.firstUsingBinarySearchIndexed(predicate: (IndexedValue<T>) -> Boolean): T? {
    var lowerBound = 0
    var upperBound = size
    while (lowerBound < upperBound) {
        val middle = (lowerBound + upperBound) / 2
        if (predicate(IndexedValue(middle, this[middle]))) {
            upperBound = middle // Move the search to the left part
        } else {
            lowerBound = middle + 1 // Move the search to the right part
        }
    }

    return if (lowerBound in indices && predicate(IndexedValue(lowerBound, this[lowerBound]))) {
        this[lowerBound]
    } else {
        null
    }
}
