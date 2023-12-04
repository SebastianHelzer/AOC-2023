import java.lang.Exception

class Day01: DayXX() {
    override val dayNumber: Int = 1
    override val dayString: String = "01"

    override fun part1(input: List<String>): Int {
        return try {
            input.sumOf { it.filter { it.isDigit() }.let { it.first().digitToInt()  * 10 + it.last().digitToInt() } }
        } catch (e: Exception) {
            0
        }
    }

    override fun part2(input: List<String>): Int {
        val replacementList = mapOf(
            "zero" to 0,
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )

        return input.sumOf { line ->
            val modifiedInput = replacementList.entries.fold(line) { newLine, entry -> newLine.replace(entry.key, entry.key + entry.value.toString() + entry.key) }
            modifiedInput.filter { it.isDigit() }.let { it.first().digitToInt()  * 10 + it.last().digitToInt() }
        }
    }
}

fun main() {
    Day01().runTest(listOf("_test", "_test2", ""))
}