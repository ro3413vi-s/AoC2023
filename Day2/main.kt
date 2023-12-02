import java.io.File

val red: Int = 12
val green: Int = 13
val blue: Int = 14
val colorMap: Map<String, Int> = mapOf("red" to red, "green" to green, "blue" to blue)

var accumulate: Int = 0
var accumulatePower: Int = 0

fun main() {
    var infile = File("res/input1.txt")
    infile.forEachLine{ totalPossible(it) }
    println("total accumulated: " + accumulate)
    infile.forEachLine{ totalPower(it) }
    println("total power accumulated: " + accumulatePower)
}

// Part 1

fun totalPossible(line: String) {
    val (id, rowData) = splitForId(line)
    if(handleDraws(rowData)) {
        accumulate += id
    }
}

fun handleDraws(rowData: String) : Boolean {
    var draws = splitDraws(rowData)
    for (draw in draws) {
        for (d in draw) {
            val drawNumber = d[1].toInt()
            val maxNumber: Int = colorMap.get(d[0]) ?: 0
            if ( drawNumber > maxNumber) {
                return false
            }
        }
    }
    return true
}

// Part 2

fun totalPower(line: String) {
    val (_, res) = splitForId(line)
    var draws = splitDraws(res)
    var maxVals: MutableMap<String,Int> = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
    for(draw in draws) {
        for (d in draw) {
            var type: String = d[0]
            var value: Int = d[1].toInt()
            if (maxVals.getValue(type) < value) {
                maxVals[type] = value
            }
        }
    }
    accumulatePower += maxVals.values.reduce{ acc, it -> acc * it }
}

// Helper functions

fun splitForId(line: String): Pair<Int, String> {
    var lineList: ArrayList<String> = line.split(':') as ArrayList<String>
    lineList[0] = lineList[0].filter{ it.isDigit() }
    return Pair(lineList[0].toInt(), lineList[1])
}

/** input: 
    5 red, 1 green, 2 blue; 
    2 green, 8 blue, 6 red
    output: 
    [[[red, 5], [green, 1], [blue, 2]], 
    [[green, 2], [blue, 8], [red, 6]]]
 */
fun splitDraws(row: String): List<List<List<String>>> {
    return row.split(';').map{ it.split(',') }.map{ it.map{ it.trim().split(' ').reversed() } }
}
