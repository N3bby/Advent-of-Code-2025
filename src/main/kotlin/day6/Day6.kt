package day6

import ext.transpose

data class CephalodProblem(val numbers: List<Long>, val operation: Long.(Long) -> Long) {

    fun solve(): Long {
        return numbers.reduce { acc, number -> acc.operation(number) }
    }

    companion object {
        fun from(list: List<String>): CephalodProblem {
            val numbers = list.dropLast(1).map { it.toLong() }
            val operation: Long.(Long) -> Long = when (list.last()) {
                "+" -> Long::plus
                "*" -> Long::times
                else -> throw IllegalArgumentException("Unknown operator: ${list.last()}")
            }

            return CephalodProblem(numbers, operation)
        }
    }
}

fun parseCaphalodProblems(input: String): List<CephalodProblem> = input.lines()
    .map { line -> line.split(" ").filter { it.isNotBlank() } }
    .transpose()
    .map { CephalodProblem.from(it) }
