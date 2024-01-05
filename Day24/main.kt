import java.io.File

data class Line(var m: Double, var b: Double, var x: Double, var dx: Double)
var lines: MutableList<Line> = mutableListOf()

fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine{
        var t = it.split('@').map{it.trim().split(',').map{it.trim().toDouble()}}
        var m = t[1][1] / t[1][0]
        var b = t[0][1] - (m*t[0][0])
        lines.add(Line(m, b, t[0][0], t[1][0]))
    }
    //println(lines)
    var minPos: Double = 200000000000000.0
    var maxPos: Double = 400000000000000.0
    var sum = 0
    for(l1 in lines) {
        for (l2 in lines) {
           var t = intersect(l1,l2)
           if(t != null) {
                if (t.first in minPos..maxPos && t.second in minPos..maxPos) {
                    if( Math.abs(l1.x - t.first) > Math.abs(l1.x+l1.dx - t.first) &&
                        Math.abs(l2.x - t.first) > Math.abs(l2.x+l2.dx - t.first)
                    ) {
                        sum++
                    }
                }
           }
        }
    }
    println(sum/2)
}


fun intersect(l1: Line, l2: Line): Pair<Double,Double>? {
    if(l1.m == l2.m){
        return null
    }
    var x = (l2.b - l1.b) / (l1.m - l2.m);
    var y = l1.m * x + l1.b;
    return Pair(x,y);
}