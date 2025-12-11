package day10

fun String.removeParentheses(): String = this.replace(Regex("[\\[\\]{}()]"), "")

fun parseIndicators(input: String): List<Boolean> = input
    .removeParentheses()
    .toCharArray()
    .map {
        when (it) {
            '.' -> false
            '#' -> true
            else -> throw IllegalArgumentException("Invalid indicator character: $it")
        }
    }

fun parseButton(input: String): Button = input
    .removeParentheses()
    .split(",")
    .map(String::toInt)

fun parseJoltageRequirements(input: String): List<Int> = input
    .removeParentheses()
    .split(",")
    .map(String::toInt)

fun parseMachines(input: String): List<Machine> = input
    .lines()
    .map { it.split(" ") }
    .map {
        val indicators = it.first().let(::parseIndicators)
        val buttons = it.drop(1).dropLast(1).map(::parseButton)
        val joltageRequirements = it.last().let(::parseJoltageRequirements)
        Machine(indicators, buttons, joltageRequirements)
    }

typealias Button = List<Int>
typealias ButtonOrder = List<Button>
typealias ReachedStateAfter = Int

data class Machine(
    val requiredIndicators: List<Boolean>,
    val buttons: List<Button>,
    val joltageRequirements: List<Int>,
) {

    fun applyButtonPressesForIndicators(buttonOrder: ButtonOrder): ReachedStateAfter? {
        val state = MutableList(requiredIndicators.size) { false }
        buttonOrder.forEachIndexed { idx, button ->
            button.forEach { state[it] = !state[it] }
            if (state == requiredIndicators) return idx + 1
        }
        return null
    }

    private val buttonOrderCache = mutableMapOf<ButtonOrder, List<Int>>()

    // I'm not sure this will work
    // You need to sorta combine joltages from the cache with the current count
    // And you also need to eliminate paths which you know for sure won't help anymore
    // Maybe some sort of breadth first search where you can eliminate branches or a queue system
    fun applyButtonPressesForJoltage(
        fullButtonOrder: ButtonOrder,
        buttonOrder: ButtonOrder,
        joltages: List<Int> = MutableList(joltageRequirements.size) { 0 },
        buttonPresses: Int = 0,
    ): ReachedStateAfter? {
        if (buttonOrderCache.contains(buttonOrder)) {

            return buttonPresses + buttonOrderCache[fullButtonOrder]!!.indexOfFirst(joltages::contains)
        }
        if (buttonOrder.isEmpty()) return null

        val newJoltages = joltages.toMutableList()
        buttonOrder.first().forEach { newJoltages[it]++ }

        if (newJoltages == joltageRequirements) {
            buttonOrderCache[fullButtonOrder] = newJoltages
            return buttonPresses + 1
        }

        val reachedAfter =
            applyButtonPressesForJoltage(fullButtonOrder, buttonOrder.drop(1), newJoltages, buttonPresses + 1)
        if(reachedAfter)
        return reachedAfter
    }

    fun getFewestButtonPressesForIndicators(): Int = buttons
        .generateAllPermutations()
        .first { applyButtonPressesForIndicators(it) !== null }
        .let { applyButtonPressesForIndicators(it)!! }

    fun getFewestButtonPressesForJoltage(): Int = buttons
        .generateAllPermutations(Int.MAX_VALUE, true)
        .first { applyButtonPressesForJoltage(it, it) !== null }
        .let { applyButtonPressesForJoltage(it, it)!! }
}

fun <T> List<T>.generateAllPermutations(maxSize: Int = size, allowRepetition: Boolean = false): Sequence<List<T>> =
    sequence {
        for (depth in 1..maxSize) {
            yieldAll(getAllPermutations(depth, allowRepetition))
        }
    }

fun <T> List<T>.getAllPermutations(depth: Int, allowRepetition: Boolean): List<List<T>> {
    if (depth == 0) return listOf(emptyList())
    if (isEmpty()) return emptyList()

    return this.flatMap { element ->
        val remaining = if (allowRepetition) this else (this - element)
        remaining.getAllPermutations(depth - 1, allowRepetition).map { listOf(element) + it }
    }
}


