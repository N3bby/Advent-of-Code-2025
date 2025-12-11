package day10

import ext.transpose
import org.ojalgo.optimisation.ExpressionsBasedModel
import org.ojalgo.optimisation.Optimisation
import kotlin.math.abs
import kotlin.math.roundToInt

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

    fun getFewestButtonPressesForIndicators(): Int = buttons
        .generateAllPermutations()
        .first { applyButtonPressesForIndicators(it) !== null }
        .let { applyButtonPressesForIndicators(it)!! }

    /**
     * I had to throw in the towel on this one. This solution is generated using AI (with multiple back-and-forth steps)
     * The problem goes far beyond my own skill-set.
     * I understand the general concept of what is happening, but the details are honestly too complex for me.
     * */
    // Integer Linear Programming version using ojAlgo
    // Minimises sum of button presses with Ax = b and x ∈ Z>=0
    fun getFewestButtonPressesForJoltageILP(): Int {
        val a = buttons
            .map { button -> joltageRequirements.mapIndexed { idx, _ -> if (idx in button) 1.0 else 0.0 } }
            .transpose()
            .map { it.toDoubleArray() }
            .toTypedArray()
        val b = joltageRequirements.map { it.toDouble() }.toDoubleArray()

        return fewestPressesILP(a, b)
    }
}

// ojAlgo ILP helper: minimise sum(x) s.t. A x = b, x integer >= 0
fun fewestPressesILP(A: Array<DoubleArray>, b: DoubleArray): Int {
    val n = A[0].size
    val m = A.size
    val model = ExpressionsBasedModel()

    val vars = (0 until n).map { j ->
        model.addVariable("x$j")
            .lower(0)
            .integer(true)
            .weight(1.0) // objective coefficient 1 for minimisation
    }

    for (i in 0 until m) {
        val expr = model.addExpression("row$i").level(b[i])
        for (j in 0 until n) {
            val coef = A[i][j]
            if (coef != 0.0) expr.set(vars[j], coef)
        }
    }

    val res = model.minimise()
    val state = res.state
    if (
        state != Optimisation.State.OPTIMAL &&
        state != Optimisation.State.FEASIBLE &&
        state != Optimisation.State.DISTINCT
    ) {
        throw IllegalStateException("No integer solution: $state")
    }

    // Extract variable values, round to nearest integer (avoid truncation), and verify Ax == b
    // In ojAlgo 55, Result.get expects a variable index (Long)
    val x = IntArray(n) { j ->
        val v = vars[j]
        val idx = try {
            val indexField = v.javaClass.getMethod("index").invoke(v) as? Number
                ?: v.javaClass.getMethod("getIndex").invoke(v) as? Number
            (indexField ?: error("Variable index not available")).toLong()
        } catch (e: Exception) {
            vars.indexOf(v).toLong()
        }
        val raw = res.get(idx)
        val d = when (raw) {
            is Number -> raw.toDouble()
            else -> raw.toString().toDouble()
        }
        d.roundToInt()
    }

    // Verify feasibility: A x == b within tiny epsilon
    val eps = 1e-8
    for (i in 0 until m) {
        var lhs = 0.0
        for (j in 0 until n) lhs += A[i][j] * x[j]
        require(abs(lhs - b[i]) <= eps) { "Rounded solution violates row $i: lhs=$lhs rhs=${b[i]}" }
    }

    return x.sum()
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


