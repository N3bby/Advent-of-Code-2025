package ext

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringExtensionsKtTest {

    @Test
    fun string_splitInTwo() {
        assertEquals("abcd".splitInTwo(), Pair("ab", "cd"))
    }

    @Test
    fun string_transpose() {
        val input = """
            [D]    
        [N] [C]    
        [Z] [M] [P]
         1   2   3
        """.trimIndent()

        val expected = """
         [[ 
         NZ1
         ]] 
            
        [[[ 
        DCM2
        ]]] 
            
          [ 
          P3
          ] 
            
        """.trimIndent()

        assertEquals(expected, input.transpose())
    }

    @Test
    fun string_reverseLines() {
        val input = """
            abc
            def
        """.trimIndent()

        val expected = """
            cba
            fed
        """.trimIndent()

        assertEquals(expected, input.reversedPerLine())
    }

    @Test
    fun string_hasSubstringAt() {
        assertThat("helloWorld".hasSubstringAt(0, "hello")).isTrue()
        assertThat("123helloWorld".hasSubstringAt(0, "hello")).isFalse()
        assertThat("123helloWorld".hasSubstringAt(3, "hello")).isTrue()
    }

    @Test
    fun string_paddedLines() {
        val input = """
            abcdef
            def
        """.trimIndent()

        assertThat(input.paddedLines()).isEqualTo(listOf(
            "abcdef",
            "def   "
        ))
    }
}
