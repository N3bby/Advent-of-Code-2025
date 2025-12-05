package day5

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.readInput

class Day5KtTest {

    @Test
    fun `part 1 - example input`() {
        val input = """
            3-5
            10-14
            16-20
            12-18
            
            1
            5
            8
            11
            17
            32
        """.trimIndent()

        val (freshIngredients, availableIngredients) = parseFreshIngredientsAndAvailableIngredients(input)
        val availableFreshIngredients = availableIngredients.count { freshIngredients.contains(it) }

        assertThat(availableFreshIngredients).isEqualTo(3)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(5)

        val (freshIngredients, availableIngredients) = parseFreshIngredientsAndAvailableIngredients(input)
        val availableFreshIngredients = availableIngredients.count { freshIngredients.contains(it) }

        assertThat(availableFreshIngredients).isEqualTo(798)
    }
}

