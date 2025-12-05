package day5

import ext.difference
import ext.removeOverlaps
import ext.size
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

    @Test
    fun `part 2 - example input`() {
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

        val (freshIngredients, _) = parseFreshIngredientsAndAvailableIngredients(input)
        val possibleTotalFreshIngredients = freshIngredients.removeOverlaps().sumOf { it.size() }

        assertThat(possibleTotalFreshIngredients).isEqualTo(14)
    }

    @Test
    fun `part 2 - puzzle input`() {
        val input = readInput(5)

        val (freshIngredients, _) = parseFreshIngredientsAndAvailableIngredients(input)
        val availableFreshIngredients = freshIngredients.removeOverlaps().sumOf { it.size() }

        assertThat(availableFreshIngredients).isEqualTo(366181852921027)
    }

}

