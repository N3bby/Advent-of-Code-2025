package day10

import ext.transpose
import org.apache.commons.math3.optim.MaxIter
import org.apache.commons.math3.optim.linear.*
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType

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

    // Using a constraint solver (with a lot of help from Claude)
    // I should try and figure out how this works exactly
    // And then I'll have to try and map our Machine to these inputs
    // And hopefully this runs fast enough...

    fun getFewestButtonPressesForJoltageLA(): Int {
        val coefficients = buttons
            .map { button -> joltageRequirements.mapIndexed { idx, _ -> if (idx in button) 1.0 else 0.0 } }
            .transpose()
            .map { it.toDoubleArray() }
            .toTypedArray()
        val constants = joltageRequirements
            .map { it.toDouble() }
            .toDoubleArray()

        val solution = solveMinimizingSum(coefficients, constants)
            ?: throw Error("No solution found")

        return solution.sum().toInt()
    }
}

fun solveMinimizingSum(
    coefficients: Array<DoubleArray>,
    constants: DoubleArray,
): DoubleArray? {
    try {
        // Objective: minimize a + b + c + d
        // All coefficients are 1 for the sum
        val objectiveCoefficients = DoubleArray(coefficients[0].size) { 1.0 }
        val objective = LinearObjectiveFunction(objectiveCoefficients, 0.0)

        // Constraints: Ax = b (equality constraints)
        val constraints = mutableListOf<LinearConstraint>()
        for (i in coefficients.indices) {
            constraints.add(
                LinearConstraint(
                    coefficients[i],
                    Relationship.EQ,
                    constants[i]
                )
            )
        }

        // Optional: Add non-negativity constraints
        for (i in coefficients[0].indices) {
            val constraint = DoubleArray(coefficients[0].size) { j -> if (i == j) 1.0 else 0.0 }
            constraints.add(LinearConstraint(constraint, Relationship.GEQ, 0.0))
        }

        val solver = SimplexSolver()
        val solution = solver.optimize(
            objective,
            LinearConstraintSet(constraints),
            GoalType.MINIMIZE,
            MaxIter(1e6.toInt())
        )

        return solution.point
    } catch (e: Exception) {
        println("Error solving: ${e.message}")
        return null
    }
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


