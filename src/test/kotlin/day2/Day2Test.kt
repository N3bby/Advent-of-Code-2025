package day2

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import util.readInput

class Day2KtTest {

    @Test
    fun `isValidProductIdPart1 should be correct`() {
        assertSoftly {
            it.assertThat(11L.isInvalidProductIdPart1()).isTrue()
            it.assertThat(99L.isInvalidProductIdPart1()).isTrue()
            it.assertThat(1010L.isInvalidProductIdPart1()).isTrue()
            it.assertThat(1188511885L.isInvalidProductIdPart1()).isTrue()
            it.assertThat(222222L.isInvalidProductIdPart1()).isTrue()
            it.assertThat(446446L.isInvalidProductIdPart1()).isTrue()
            it.assertThat(38593859L.isInvalidProductIdPart1()).isTrue()
        }
    }

    @Test
    fun `part 1 - example input`() {
        val input =
            "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"

        val sumOfInvalidIds = parseProductIds(input)
            .filter { it.isInvalidProductIdPart1() }
            .sum()

        assertThat(sumOfInvalidIds).isEqualTo(1227775554)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(2)

        val sumOfInvalidIds = parseProductIds(input)
            .filter { it.isInvalidProductIdPart1() }
            .sum()

        assertThat(sumOfInvalidIds).isEqualTo(34826702005)
    }

    @Test
    fun `isValidProductIdPart2 should be correct`() {
        assertSoftly {
            it.assertThat(11L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(99L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(1010L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(1188511885L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(222222L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(446446L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(38593859L.isInvalidProductIdPart2()).isTrue()

            // Added with part 2
            it.assertThat(111L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(999L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(565656L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(824824824L.isInvalidProductIdPart2()).isTrue()
            it.assertThat(2121212121L.isInvalidProductIdPart2()).isTrue()
        }
    }

    @Test
    fun `part 2 - example input`() {
        val input =
            "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"

        val sumOfInvalidIds = parseProductIds(input)
            .filter { it.isInvalidProductIdPart2() }
            .sum()

        assertThat(sumOfInvalidIds).isEqualTo(4174379265)
    }

    @Test
    fun `part 2 - puzzle input`() {
        val input = readInput(2)

        val sumOfInvalidIds = parseProductIds(input)
            .filter { it.isInvalidProductIdPart2() }
            .sum()

        assertThat(sumOfInvalidIds).isEqualTo(43287141963)
    }

}

