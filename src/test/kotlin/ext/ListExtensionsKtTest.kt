package ext

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ListExtensionsKtTest {

    @Test
    fun `getAllUniqueCombinations - returns all unique combinations`() {
        val uniqueCombinations = listOf(1, 2, 3).getAllUniqueCombinations()
        assertThat(uniqueCombinations).containsExactlyInAnyOrder(
            Pair(1, 2),
            Pair(1, 3),
            Pair(2, 3),
        )
    }

    @Test
    fun transpose() {
        val transposed = listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6)
        ).transpose()

        assertThat(transposed).isEqualTo(
            listOf(
                listOf(1, 4),
                listOf(2, 5),
                listOf(3, 6)
            )
        )
    }
}

