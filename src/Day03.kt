import kotlin.time.measureTime
import kotlin.time.measureTimedValue

typealias Position = Pair<Int, Int>
typealias SymbolWithPosition = Pair<Position, Char>
typealias NumberWithPositions = Pair<List<Position>, Int>

class Day03: DayXX() {
    override val dayNumber: Int = 3
    override val dayString: String = "03"

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

    override fun part1(input: List<String>): Int {
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

    override fun part2(input: List<String>): Int {
        val (digits, parts) = getDigitsAndParts(input)
        val numbers = mapDigitsToNumbers(digits)
        val gearRatios = getGearRatios(numbers, parts)
        return gearRatios.sum()
    }
}

fun main() {
    Day03().runTest()
}