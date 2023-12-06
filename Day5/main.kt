import java.io.File

data class tableRow(var from: Long, var destination: Long, var size: Long)
var tables: MutableList<MutableList<tableRow>> = mutableListOf()

var seeds: MutableList<Long> = mutableListOf()
var seedRanges: MutableList<Pair<Long,Long>> = mutableListOf()


fun main() {
    val inFile = File("res/input.txt")
    
    var inData = parseData(inFile.readLines().toMutableList())
    parseSeeds(inData[0])
    parseTables(inData.subList(1,inData.lastIndex))
    var locations = calculateLocations(seeds, 0)
    
    var i: Int = 0
    var ranges = seedRanges
    while(i <= tables.lastIndex) {
        var temp = calculateLocationsRanges(ranges, tables[i])
        ranges.clear()
        ranges = temp
        i++
    }
    println("--- results ---")
    println(locations.min())
    println(ranges)
}

fun parseData(list: MutableList<String>): MutableList<List<String>> {
    var returnList: MutableList<List<String>> = mutableListOf()
    var tempList: MutableList<String> = list
    while(!tempList.isEmpty()) {
        tempList.removeLast()
        returnList.add(tempList.takeLastWhile{ !it.isEmpty() })
        tempList = tempList.dropLastWhile{ !it.isEmpty() }.toMutableList()
    }
    returnList = returnList.asReversed()
    return returnList
}

fun parseSeeds(input: List<String>) {
    var seedInput = input[0].split(' ').drop(1)
    seeds = seedInput.map{ it.toLong() }.toMutableList()
    while(!seedInput.isEmpty()) {
        seedRanges.add(seedInput.take(2).map{ it.toLong() }.zipWithNext()[0])
        seedInput = seedInput.drop(2)
    }
}

fun parseTables(input: MutableList<List<String>>) {
    for(i in input) {
        var newTable: MutableList<tableRow> = mutableListOf()
        var table = i.drop(1)
        table.forEach{ 
            var t = it.trim().split(' ')
            newTable.add(tableRow(t[1].toLong(), t[0].toLong(), t[2].toLong()))
        }
        tables.add(newTable)
    }
}

// Part 1
fun calculateLocations(iteration: MutableList<Long>, index: Int): MutableList<Long> {
    if(index > tables.lastIndex) {
        return iteration
    }
    var tempData = iteration
    var res: MutableList<Long> = mutableListOf()
    for(td in tempData) {
        var table = tables[index]
        var found: Boolean = false
        table.forEach {
            if(td >= it.from && td < it.from + it.size) {
                var delta = td - it.from
                res.add(it.destination + delta)
                found = true
            }
        }
        if(!found){
            res.add(td)
        }
    }
    return calculateLocations(res, index+1)
}

// Part 2

fun calculateLocationsRanges(iteration: MutableList<Pair<Long,Long>>, table: MutableList<tableRow>): MutableList<Pair<Long,Long>> { 
    var tempData: MutableList<Pair<Long,Long>> = iteration
    var res: MutableList<Pair<Long,Long>> = mutableListOf() // A
    var unusedRegion: MutableList<Pair<Long,Long>> = mutableListOf() // NR
    table.forEach {
        for(td in tempData) {
            var dataStart = td.first
            var dataEnd = td.first + td.second
            var tableStart = it.from
            var tableEnd = it.from + it.size

            var before: Pair<Long,Long> = Pair(dataStart, minOf(dataEnd, tableStart))
            var between: Pair<Long,Long> = Pair(maxOf(dataStart, tableStart), minOf(dataEnd, tableEnd))
            var after: Pair<Long,Long> = Pair(maxOf(tableEnd, dataStart), dataEnd)
            if(before.second>before.first) {
                unusedRegion.add(before)
            }
            if(between.second>between.first) {
                res.add(Pair(between.first-it.from+it.destination, between.second-it.from+it.destination))
            }
            if(after.second>after.first) {
                unusedRegion.add(after)
            }
        }
        tempData.clear()
        tempData.addAll(unusedRegion)
        unusedRegion.clear()
    }
    res.addAll(tempData)
    return res
}

