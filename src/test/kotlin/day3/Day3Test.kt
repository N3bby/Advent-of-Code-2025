package day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.readInput

class Day3KtTest {

    @Test
    fun test() {
        assertThat(listOf(1, 1, 9, 2, 1).findLargestJoltage(4)).isEqualTo(1921)
        assertThat(listOf(9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1).findLargestJoltage()).isEqualTo(98)
        assertThat(listOf(8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9).findLargestJoltage()).isEqualTo(89)
        assertThat(listOf(2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 7, 8).findLargestJoltage()).isEqualTo(78)
        assertThat(listOf(8, 1, 8, 1, 8, 1, 9, 1, 1, 1, 1, 2, 1, 1, 1).findLargestJoltage()).isEqualTo(92)
    }

    @Test
    fun `part 1 - example input`() {
        val input = """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
        """.trimIndent()

        val largestJoltage = parseBatteryBanks(input).findLargestTotalJoltage()

        assertThat(largestJoltage).isEqualTo(357)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(3)

        val largestTotalJoltage = parseBatteryBanks(input).findLargestTotalJoltage()

        assertThat(largestTotalJoltage).isEqualTo(17445)
    }

    @Test
    fun `part 2 - example input`() {
        val input = """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
        """.trimIndent()

        val largestJoltage = parseBatteryBanks(input).findLargestTotalJoltage(12)

        assertThat(largestJoltage).isEqualTo(3121910778619)
    }

    @Test
    fun `part 2 - puzzle input`() {
        val input = readInput(3)

        val largestTotalJoltage = parseBatteryBanks(input).findLargestTotalJoltage(12)

        assertThat(largestTotalJoltage).isEqualTo(173229689350551)
    }

}

