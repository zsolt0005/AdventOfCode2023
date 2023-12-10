fun main() {
    val input = readInput("Day05")

    Day05(input).part1().printlnWithPrefix("Part1")
    Day05(input).part2().printlnWithPrefix("Part2")
}

class Day05(private val lines: List<String>)
{
    fun part1(): Long
    {
        val data = prepareData()
        val locations = getLocationsBySeeds(data)

        return locations.minOf { it.min }
    }

    fun part2(): Long
    {
        val data = prepareData()
        val locations = getLocationsBySeedRanges(data)

        return locations.minOf { it.min }
    }

    private fun prepareData(): Almanac
    {
        val seeds = lines.first().substringAfter(":").trim().split(" ").map { it.toLong() }

        return Almanac(
            seeds,
            prepareMap("seed-to-soil"),
            prepareMap("soil-to-fertilizer"),
            prepareMap("fertilizer-to-water"),
            prepareMap("water-to-light"),
            prepareMap("light-to-temperature"),
            prepareMap("temperature-to-humidity"),
            prepareMap("humidity-to-location")
        )
    }

    private fun prepareMap(name: String): Map<Range, Long>
    {
        val mapStart = lines.indexOfFirst { it.startsWith(name) } + 1

        val map = mutableMapOf<Range, Long>()
        lines
            .subList(mapStart, lines.size)
            .takeWhile { it.isNotEmpty() }
            .forEach {
                val values = it.split(" ").map { value -> value.toLong() }
                map[Range(values[1], values[1] + values[2])] = values[0]
            }

        return map
    }

    private fun getLocationsBySeeds(data: Almanac): List<Range>
    {
        return data.seeds.map { applyMapsToSeedRanges(data, listOf(Range(it, it))) }.flatten()
    }

    private fun getLocationsBySeedRanges(data: Almanac): List<Range>
    {
        return data.seeds.chunked(2).map { (from, range) -> applyMapsToSeedRanges(data, listOf(Range(from, from + range))) }.flatten()
    }

    private fun applyMapsToSeedRanges(almanac: Almanac, seedRange: List<Range>): List<Range>
    {
        val soilRanges = applyMapToSeedRanges(almanac.seedToSoilMap, seedRange)
        val fertilizerRanges = applyMapToSeedRanges(almanac.soilToFertilizerMap, soilRanges)
        val waterRanges = applyMapToSeedRanges(almanac.fertilizerToWaterMap, fertilizerRanges)
        val lightRanges = applyMapToSeedRanges(almanac.waterToLightMap, waterRanges)
        val temperatureRanges = applyMapToSeedRanges(almanac.lightToTemperatureMap, lightRanges)
        val humidityRanges = applyMapToSeedRanges(almanac.temperatureToHumidityMap, temperatureRanges)
        return applyMapToSeedRanges(almanac.humidityToLocationMap, humidityRanges)
    }

    private fun applyMapToSeedRanges(map: Map<Range, Long>, ranges: List<Range>): List<Range>
    {
        return ranges.flatMap { (min, max) ->
            val rangeDest = mutableListOf<Range>()

            val inter = mutableListOf<Range>()
            for ((range, dest) in map) {
                val rmin = maxOf(min, range.min)
                val rmax = minOf(max, range.max)
                if (rmin <= rmax) {
                    inter.add(Range(rmin, rmax))
                    rangeDest.add(Range(rmin - range.min + dest, rmax - range.min + dest))
                }
            }
            inter.sortBy { it.min }
            var pivot = min
            for ((rmin, rmax) in inter) {
                if (rmin > pivot) {
                    rangeDest.add(Range(pivot, rmin - 1))
                }
                pivot = rmax + 1
            }
            if (pivot <= max) {
                rangeDest.add(Range(pivot, max))
            }

            rangeDest
        }
    }
}

data class Almanac(
    val seeds: List<Long> = listOf(),
    val seedToSoilMap: Map<Range, Long> = mapOf(),
    val soilToFertilizerMap: Map<Range, Long> = mapOf(),
    val fertilizerToWaterMap: Map<Range, Long> = mapOf(),
    val waterToLightMap: Map<Range, Long> = mapOf(),
    val lightToTemperatureMap: Map<Range, Long> = mapOf(),
    val temperatureToHumidityMap: Map<Range, Long> = mapOf(),
    val humidityToLocationMap: Map<Range, Long> = mapOf()
)

data class Range(val min: Long, val max: Long)