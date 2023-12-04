import kotlin.time.measureTime
import kotlin.time.measureTimedValue

abstract class DayXX {
    abstract val dayNumber: Int
    abstract val dayString: String

    abstract fun part1(input: List<String>): Any
    abstract fun part2(input: List<String>): Any

    fun runTest() {
        val testInput = readInput("Day${dayString}_test")
        val warmUp = measureTime { (0..1000).map { it * it } }
        println("warm up in $warmUp for Day${dayString}")
        val part1TestTime = measureTimedValue { part1(testInput) }
        println("Part 1 test ${part1TestTime.value} in ${part1TestTime.duration}")
        val part2TestTime = measureTimedValue { part2(testInput) }
        println("Part 2 test ${part2TestTime.value} in ${part2TestTime.duration}")
        val input = readInput("Day${dayString}")
        val part1Time = measureTimedValue { part1(input) }
        println("Part 1 ${part1Time.value} in ${part1Time.duration}")
        val part2Time = measureTimedValue { part2(input) }
        println("Part 2 ${part2Time.value} in ${part2Time.duration}")
    }
}
