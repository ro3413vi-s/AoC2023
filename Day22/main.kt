import java.io.File

data class Brick(var x1: Int, var y1: Int, var z1: Int, var x2: Int, var y2: Int, var z2: Int)
var brickList: MutableList<Brick> = mutableListOf()

fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine { line ->
        var b = line.split(',', '~').map{ it.toInt()}
        brickList.add(Brick(b[0],b[1],b[2],b[3],b[4],b[5])) 
    }
    brickList.sortWith(compareBy<Brick>{ minOf(it.z1, it.z2) }.thenBy{ maxOf(it.z1, it.z2) })
    shiftBricksDown(brickList,-1)
    
    var moveList = mutableListOf<Boolean>()
    var peakList = mutableListOf<Int>()
    for(index in 0..brickList.lastIndex) {
        var t = shiftBricksDown(brickList.toMutableList(), skip = index)
        moveList.add(t.first)
        peakList.add(t.second)
    }
    println(moveList.count{it == true})
    println(peakList.sum())
}

fun shiftBricksDown(bricks: MutableList<Brick>, skip: Int): Pair<Boolean, Int> {
    var peaks: MutableMap<Pair<Int,Int>,Int> = mutableMapOf()
    var falls = 0
    for(index in 0..bricks.lastIndex) {
        if(index == skip) continue
        
        var brick = bricks[index]
        var area: MutableList<Pair<Int,Int>> = mutableListOf()

        for(x in brick.x1..brick.x2) {
            for(y in brick.y1..brick.y2) {
                area.add(Pair(x,y))
            }
        }

        var peak = 1 + (peaks.filter{ (k,_) -> k in area }.values.maxOrNull() ?: 0)
        var peakDelta = peak + brick.z2 - brick.z1
        for (a in area) peaks[a] = peakDelta

        bricks[index] = Brick(brick.x1, brick.y1, peak, brick.x2, brick.y2, peakDelta)
        if(peak < brick.z1){
            falls += 1
        }
    }
    return Pair(falls == 0, falls)
}