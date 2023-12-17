import java.io.File

var graph: MutableList<MutableList<Int>> = mutableListOf()
var scores: MutableMap<ScoreItem,Int> = mutableMapOf()
var unvisited: MutableList<HeapItem> = mutableListOf(HeapItem(0,0,0,0,0,0))

var directions: List<Pair<Int,Int>> = listOf(Pair(1,0),Pair(-1,0),Pair(0,1),Pair(0,-1))

var width: Int = -1
var height: Int = -1

data class HeapItem(var sum: Int, var x: Int, var y: Int, var xi: Int, var yi: Int, var steps: Int)
data class ScoreItem(var x: Int, var y: Int, var xi: Int, var yi: Int, var steps: Int)

fun main() {
    var inFile = File("input.txt")
    var row = 0
    inFile.forEachLine{
        graph.add(mutableListOf())
        graph[row].addAll(it.map{ s -> s.toString().toInt()})
        row++
    }
    height = graph.lastIndex
    width = graph[0].lastIndex
    scores.put(ScoreItem(0,0,0,0,0), 0)
    modDjikstra(1,3)
    // Part 2
    unvisited = mutableListOf(HeapItem(0,0,0,0,0,0))
    scores.clear()
    scores.put(ScoreItem(0,0,0,0,0), 0)

    modDjikstra(4,10)
}

fun modDjikstra(minstep: Int, maxstep: Int) {
    while(!unvisited.isEmpty()) {
        var current: HeapItem = popHeap()
        if(current.x == width && current.y == height) {
            println(current.sum)
            break // we found sink
        }
        for ((nxi, nyi) in directions) {
            var nx = current.x + nxi
            var ny = current.y + nyi
            if (!(0..width).contains(nx) || !(0..height).contains(ny)) continue
            var nstep = if (nxi == current.xi && nyi == current.yi) current.steps+1 else 1
            var nsum = current.sum + graph[nx][ny]
            var newHeap = HeapItem(nsum, nx, ny, nxi, nyi, nstep)
            if( (current.xi != -nxi || current.yi != -nyi) && 
                (current.steps >= minstep || (current.xi == nxi && current.yi == nyi) || (current.xi == 0 && current.yi == 0) ) &&
                (current.steps < maxstep || (current.xi * nxi + current.yi * nyi) == 0) 
            ) {
                var scoreItm = ScoreItem(newHeap.x,newHeap.y,newHeap.xi,newHeap.yi,newHeap.steps)
                if( nsum < scores[scoreItm] ?: Int.MAX_VALUE ) {
                    unvisited.add(newHeap)
                    scores[scoreItm] = nsum
                }
            }
        }
    }
}

fun popHeap(): HeapItem {
    var minSum = Int.MAX_VALUE
    var temp: HeapItem = HeapItem(0,0,0,0,0,0)
    for (item in unvisited) {
        if (item.sum < minSum) {
            minSum = item.sum
            temp = item
        }
    }
    unvisited.remove(temp)
    return temp
}