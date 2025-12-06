package ext

fun <T> List<T>.steppedBy(step: Int, offset: Int = 0): List<T> {
    return this.filterIndexed { index, _ -> index % step == offset }
}

fun List<Int>.multiplication(): Int = reduce { acc, number -> acc * number }
fun List<Long>.multiplication(): Long = reduce { acc, number -> acc * number }

fun <T> List<T>.multiplicationOf(block: (T) -> Int): Int = this.map(block).multiplication()

fun <T> List<T>.rotateLeft(n: Int) = drop(n) + take(n)
fun <T> List<T>.rotateRight(n: Int) = takeLast(n) + dropLast(n)

fun <T: Comparable<T>> List<T>.getAllUniqueCombinations(): Set<Pair<T, T>> {
    return this
        .flatMap { outer -> map { inner -> Pair(outer, inner) } }
        .filter { (val1, val2) -> val1 != val2 }
        .map { it.sort() }
        .toSet()
}

fun <T> List<T>.dropAt(index: Int): List<T> = toMutableList().also { it.removeAt(index) }

fun <T> List<T>.allIndexed(predicate: (index: Int, value: T) -> Boolean): Boolean {
    return filterIndexed { index, value -> predicate(index, value) }.size == size
}

fun <T, R> List<T>.accumulationMap(mapper: (acc: List<T>) -> R): List<R> {
    return indices.map { index ->
        mapper(take(index + 1))
    }
}

fun <T : Comparable<T>> Iterable<T>.maxWithIndex(): Pair<T, Int> {
    return maxOrNull()
        ?.let { Pair(it, indexOf(it)) }
        ?: throw NoSuchElementException("Collection is empty")
}

fun <T> List<List<T>>.transpose(): List<List<T>> {
    assert(all { it.size == first().size }) { "All lists must have the same size" }

    return first().mapIndexed { columnIdx, _ ->
        mapIndexed { rowIdx, _ -> get(rowIdx)[columnIdx] }
    }
}
