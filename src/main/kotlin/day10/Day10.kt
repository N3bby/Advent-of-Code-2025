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
    fun applyButtonPresses(order: ButtonOrder): ReachedStateAfter? {
        val state = MutableList(requiredIndicators.size) { false }
        order.forEachIndexed { idx, button ->
            button.forEach { state[it] = !state[it] }
            if (state == requiredIndicators) return idx + 1
        }
        return null
    }

    fun getFewestButtonPresses(): Int = buttons
        .generateAllPermutations()
        .first { applyButtonPresses(it) !== null }
        .let { applyButtonPresses(it)!! }
}

fun <T> List<T>.generateAllPermutations(): Sequence<List<T>> = sequence {
    for (depth in 1..size) {
        yieldAll(getAllPermutations(depth))
    }
}

fun <T> List<T>.getAllPermutations(depth: Int): List<List<T>> {
    if (depth == 0) return listOf(emptyList())
    if (isEmpty()) return emptyList()

    return this.flatMap { element ->
        (this - element).getAllPermutations(depth - 1).map { listOf(element) + it }
    }
}



