import java.io.File

var digList: MutableList<DigItem> = mutableListOf()
data class DigItem(var dir: Char, var length: Int, var col: String)
data class Dig(var dir: Char, var length: Int)

var digBorders: Long = 0L
var digCorners: MutableList<Pair<Long,Long>> = mutableListOf()

// shoelace algorithm and picks theorem
fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine{ 
        var ins = it.split(' ')
        digList.add(DigItem(ins[0].first(), ins[1].toInt(), ins[2])) 
    }
    // Part 1
    dig(digList.map{ Dig(it.dir, it.length)})
    shoelace()
    // Part 2
    digBorders = 0
    digCorners.clear()
    var nDigList = digList.map{ it.col.substring(2..7).chunked(5) }
    var t: MutableList<Dig> = mutableListOf()
    nDigList.forEach{ t.add(Dig(intToDir(it[1][0]), it[0].toInt(radix = 16))) }
    dig(t)
    shoelace()
}

fun intToDir(input: Char): Char {
    return when(input) {
        '0' -> 'R'
        '1' -> 'D'
        '2' -> 'L'
        '3' -> 'U'
        else -> '.'
    }
}

fun dig(digs: List<Dig>) {
    var prev: Pair<Long,Long> = Pair(0L,0L)
    digCorners.add(prev)
    for (dig in digs) {
        digBorders += dig.length
        when(dig.dir) {
            'R' -> prev = Pair(prev.first, prev.second+dig.length)
            'L' -> prev = Pair(prev.first, prev.second-dig.length)
            'U' -> prev = Pair(prev.first-dig.length, prev.second)
            'D' -> prev = Pair(prev.first+dig.length, prev.second)
            else -> println("ERROR no such direction")
        }
        digCorners.add(prev)
    }
}

fun shoelace() {
    // Shoelace formula
    var size = 0L
    for (p in digCorners.zipWithNext()) {
        var p1 = p.first
        var p2 = p.second
        size += (p1.first*p2.second) - (p1.second*p2.first)
    }
    println((Math.abs(size) + digBorders)/2 + 1)
}