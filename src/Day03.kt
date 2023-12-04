import kotlin.time.measureTime
import kotlin.time.measureTimedValue

typealias Position = Pair<Int, Int>
typealias SymbolWithPosition = Pair<Position, Char>
typealias NumberWithPositions = Pair<List<Position>, Int>

fun main() {

    fun getDigitsAndParts(input: List<String>): Pair<List<SymbolWithPosition>, List<SymbolWithPosition>> {
        val digitsAndParts = input.mapIndexed { row, s ->
            s.mapIndexed { column, c ->
                Position(row, column) to c
            }
        }.flatten().filter { it.second != '.' }

        return digitsAndParts.partition { it.second.isDigit() }
    }

    fun positionsAroundPosition(position: Position): List<Position> {
        val (row, column) = position
        return listOf(
            row + 1 to column,
            row + 0 to column,
            row - 1 to column,
            row + 1 to column + 1,
            row + 0 to column + 1,
            row - 1 to column + 1,
            row + 1 to column - 1,
            row + 0 to column - 1,
            row - 1 to column - 1,
        )
    }

    fun getNumbersAdjacentToParts(numbers: List<NumberWithPositions>, parts: List<SymbolWithPosition>) : List<Int> {
        return numbers.filter { number ->
            val positionsToCheck = number.first.map { position ->
                positionsAroundPosition(position)
            }.flatten().toSet()

            positionsToCheck.any { (row, column) ->
                parts.firstOrNull { it.first.first == row && it.first.second == column }?.second?.let {
                    !it.isDigit() && it != '.'
                } ?: false
            }
        }.map { it.second }
    }

    fun mapDigitsToNumbers(digits: List<SymbolWithPosition>): List<NumberWithPositions> {
        return digits.fold(listOf()) { acc, triple ->
            val (row, column) = triple.first
            val digit = triple.second.digitToInt()
            val lastEntry = acc.lastOrNull()
            val lastPosition = lastEntry?.first?.lastOrNull()
            lastPosition?.let { (lastRow, lastColumn) ->
                if (lastRow == row && lastColumn == column - 1) {
                    val newValue = lastEntry.second * 10 + digit
                    return@fold acc.dropLast(1).plus(lastEntry.first.plus(triple.first) to newValue)
                }
            }

            acc.plus(listOf(triple.first) to triple.second.digitToInt())
        }
    }

    fun part1(input: List<String>): Int {
        val (digits, parts) = getDigitsAndParts(input)
        val numbers = mapDigitsToNumbers(digits)
        val filteredNumbers = getNumbersAdjacentToParts(numbers, parts)
        return filteredNumbers.sum()
    }

    fun getGearRatios(numbers: List<NumberWithPositions>, parts: List<SymbolWithPosition>): List<Int> {
        return parts.filter { it.second == '*' }.mapNotNull { part ->
            val positionsToCheck = positionsAroundPosition(part.first)
            val closeNumbers = positionsToCheck.mapNotNull { position ->
                numbers.firstOrNull { it.first.contains(position) }
            }.toSet().map { it.second }
            if (closeNumbers.size == 2) {
                closeNumbers.first() * closeNumbers.last()
            } else {
                null
            }
        }
    }

    fun part2(input: List<String>): Int {
        val (digits, parts) = getDigitsAndParts(input)
        val numbers = mapDigitsToNumbers(digits)
        val gearRatios = getGearRatios(numbers, parts)
        return gearRatios.sum()
    }

    val testInput = readInput("Day03_test")
    val warmUp = measureTime { (0..1000).map { it * it } }
    println("warm up in $warmUp")
    val part1TestTime = measureTimedValue { part1(testInput) }
    println("Part 1 test ${part1TestTime.value} in ${part1TestTime.duration}")
    val part2TestTime = measureTimedValue { part2(testInput) }
    println("Part 2 test ${part2TestTime.value} in ${part2TestTime.duration}")
    val input = readInput("Day03_test")
    val part1Time = measureTimedValue { part1(input) }
    println("Part 1 ${part1Time.value} in ${part1Time.duration}")
    val part2Time = measureTimedValue { part2(input) }
    println("Part 2 ${part2Time.value} in ${part2Time.duration}")
}