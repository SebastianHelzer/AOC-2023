fun main() {

    val redLimit = 12
    val greenLimit = 13
    val blueLimit = 14

    fun getRedGreenAndBlueFromRound(round: String): Triple<Int, Int, Int> {
        var red = 0
        var green = 0
        var blue = 0
        round.split(",").forEach {
            val (count, color) = it.trim().split(" ")
            if (color == "red") red += count.toInt()
            if (color == "green") green += count.toInt()
            if (color == "blue") blue += count.toInt()
        }
        return Triple(red, green, blue)
    }

    fun isGameRoundValid(round: String): Boolean {
        val (red, green, blue) = getRedGreenAndBlueFromRound(round)
        return !(red > redLimit || green > greenLimit || blue > blueLimit)
    }

    fun areGameRoundsValid(rounds: List<String>): Boolean {
        return rounds.all { isGameRoundValid(it) }
    }

    fun getGameRoundsAndGameId(game: String): Pair<List<String>, Int> {
        val gameTitleAndRounds = game.split(";", ":")
        val gameTitle = gameTitleAndRounds.first()
        val gameId = gameTitle.filter { it.isDigit() }.toInt()
        val gameRounds = gameTitleAndRounds.drop(1)
        return gameRounds to gameId
    }

    fun part1(input: List<String>): Int {
        return input
            .mapNotNull {
                val (gameRounds, gameId) = getGameRoundsAndGameId(it)
                if (areGameRoundsValid(gameRounds)) { gameId } else { null }
            }.sum()
    }

    fun getGamePower(gameRounds: List<String>): Int {
        var maxRed = 0
        var maxGreen = 0
        var maxBlue = 0
        gameRounds.forEach {
            val (red, green, blue) = getRedGreenAndBlueFromRound(it)
            if(red > maxRed) maxRed = red
            if(green > maxGreen) maxGreen = green
            if(blue > maxBlue) maxBlue = blue
        }
        return maxRed * maxBlue * maxGreen
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val (gameRounds, _) = getGameRoundsAndGameId(it)
            getGamePower(gameRounds)
        }
    }

    check(part1(readInput("Day02_test")) == 8)
    println(part1(readInput("Day02")))
    check(part2(readInput("Day02_test")) == 2286)
    println(part2(readInput("Day02")))
}