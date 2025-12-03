package day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.readInput

class Day3KtTest {

    @Test
    fun `part 1 - findLargestJoltage`() {
        // Testing edge case
        assertThat(listOf(9, 0, 9).findLargestJoltage()).isEqualTo(99)
    }

    @Test
    fun `part 1 - example input`() {
        val input = """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
        """.trimIndent()

        val batteryBanks = parseBatteryBanks(input)
        val largestJoltage = batteryBanks.findLargestTotalJoltage()

        assertThat(largestJoltage).isEqualTo(357)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(3)

        val largestTotalJoltage = parseBatteryBanks(input).findLargestTotalJoltage()

        assertThat(largestTotalJoltage).isEqualTo(17445)
    }
}

