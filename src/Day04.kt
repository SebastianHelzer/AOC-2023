import kotlin.math.pow

class Day04: DayXX() {
    override val dayNumber: Int = 4
    override val dayString: String = "04"

    class ScratchCard(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>)

    fun scoreCard(card: ScratchCard): Int {
        val countOfMatches = card.numbers.count { card.winningNumbers.contains(it) }
        return if (countOfMatches == 0) 0
        else 2f.pow(countOfMatches - 1).toInt()
    }

    fun createCard(cardData: String): ScratchCard {
        val (title, winningNumbers, numbers) = cardData.split(":","|")
        return ScratchCard(
            title.filter { it.isDigit() }.toInt(),
            winningNumbers.split(" ").filter { it.isNotBlank() }.map { it.filter { it.isDigit() }.toInt() },
            numbers.split(" ").filter { it.isNotBlank() }.map { it.filter { it.isDigit() }.toInt() },
            )
    }

    override fun part1(input: List<String>): Any {
        return input.sumOf { scoreCard(createCard(it)) }
    }

    fun generateNewCardIds(card: ScratchCard): List<Int> {
        val countOfMatches = card.numbers.count { card.winningNumbers.contains(it) }
        return (0 until countOfMatches).map { card.id + 1 + it }
    }

    override fun part2(input: List<String>): Any = buildMap<Int, Int> {
        input.map { createCard(it) }.forEach {
            val newCardIds = generateNewCardIds(it)
            val cardCount = this.getOrPut(it.id) { 1 }
            newCardIds.forEach { cardId ->
                val oldValue = this.getOrPut(cardId) { 1 }
                this[cardId] = oldValue + cardCount
            }
        }
    }.values.sum()
}

fun main() {
    Day04().runTest()
}