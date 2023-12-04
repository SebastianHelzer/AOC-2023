import kotlin.time.measureTime
import kotlin.time.measureTimedValue

abstract class DayXX {
    abstract val dayNumber: Int
    abstract val dayString: String

    abstract fun part1(input: List<String>): Any
    abstract fun part2(input: List<String>): Any

    fun runTest(inputs: List<String> = listOf("_test", "")) {
        val warmUp = measureTime { (0..1000).map { it * it } }
        println("warm up in $warmUp for Day${dayString}")
        inputs.forEach { suffix ->
            val input = getInput(suffix)
            val part1Time = measureTimedValue { part1(input) }
            println("Part 1 $suffix ${part1Time.value} in ${part1Time.duration}")
            val part2Time = measureTimedValue { part2(input) }
            println("Part 2 $suffix ${part2Time.value} in ${part2Time.duration}")
        }
    }

    fun getInput(suffix: String = ""): List<String> {
        return readInput("Day${dayString}$suffix")
    }
}
