package day6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.Grid
import util.readInput

class Day6KtTest {

    @Test
    fun `part 1 - example input`() {
        val input = """
            123 328  51 64 
             45 64  387 23 
              6 98  215 314
            *   +   *   +  
        """.trimIndent()

        val grids = Grid.fromString(input).splitByEmptyColumns()
        val grandTotal = grids
            .map { CephalodProblem.fromGrid(it) }
            .sumOf { it.solve() }

        assertThat(grandTotal).isEqualTo(4277556)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(6)

        val grids = Grid.fromString(input).splitByEmptyColumns()
        val grandTotal = grids
            .map { CephalodProblem.fromGrid(it) }
            .sumOf { it.solve() }

        assertThat(grandTotal).isEqualTo(4277556)
    }
}

