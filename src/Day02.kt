fun main() {
    val input = readInput("Day02")

    Day02().part1(input).printlnWithPrefix("Part1")
    Day02().part2(input).printlnWithPrefix("Part2")
}

class Day02
{
    private val redCubes = 12
    private val greenCubes = 13
    private val blueCubes = 14

    fun part1(lines: List<String>): Int
    {
        val gamesData = getGamesData(lines)

        val validGames = gamesData.filter {
            val invalidSets = it.sets.filter { set -> set.red > redCubes || set.green > greenCubes || set.blue > blueCubes }

            invalidSets.isEmpty()
        }

        return validGames.sumOf { gameData -> gameData.id}
    }

    fun part2(lines: List<String>): Int
    {
        val gamesData = getGamesData(lines)

        return gamesData.sumOf { gameData ->
            val maxRed = gameData.sets.maxBy { it.red }.red
            val maxGreen = gameData.sets.maxBy { it.green }.green
            val maxBlue = gameData.sets.maxBy { it.blue }.blue

            maxRed * maxGreen * maxBlue
        }
    }

    private fun getGamesData(lines: List<String>): List<GameData>
    {
        val gamesData = mutableListOf<GameData>()

        lines.forEach { line ->
            val cleanLine = line.replace(" ", "")

            val gameIdPart = cleanLine.split(":").first()
            val setParts = cleanLine.replace("$gameIdPart:", "").split(";")

            val sets = mutableListOf<GameSetData>()

            setParts.forEach { set ->
                val cubes = set.split(",")

                var red = 0
                var green = 0
                var blue = 0

                cubes.forEach { cube ->
                    if(cube.contains("red"))
                        red += cube.replace("red", "").toInt()
                    else if(cube.contains("green"))
                        green += cube.replace("green", "").toInt()
                    else if(cube.contains("blue"))
                        blue += cube.replace("blue", "").toInt()
                }

                sets.add(GameSetData(red, green, blue))
            }

            gamesData.add(GameData(
                gameIdPart.replace("Game", "").toInt(),
                sets
            ))
        }

        return gamesData
    }
}

data class GameData(
    val id: Int,
    val sets: List<GameSetData>
)

data class GameSetData(
    val red: Int,
    val green: Int,
    val blue: Int
)