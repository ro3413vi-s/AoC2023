import java.io.File

data class Line(var m: Double, var b: Double)
data class Hail(var x: Double, var y: Double, var z: Double, var dx: Double, var dy: Double, var dz: Double, 
    var ax: Double, var ay: Double, var az: Double)
var hailStones: MutableList<Hail> = mutableListOf()

fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine{
        var t = it.split('@').map{it.trim().split(',').map{it.trim().toDouble()}}
        hailStones.add(Hail(t[0][0],t[0][1],t[0][2],t[1][0],t[1][1],t[1][2],0.0 ,0.0 ,0.0))
    }
    var minPos: Double = 200000000000000.0
    var maxPos: Double = 400000000000000.0
    //part1(7.0, 27.0) // for test input
    part1(minPos, maxPos)
    // Part 2
    part2()
}

// Part 1

fun part1(minPos: Double, maxPos: Double) {
    var sum = 0
    for(h1 in hailStones) {
        for (h2 in hailStones) {
            var t: Pair<Double?, Double?> = intersect(h1,h2)
            if(nonNull(t)) {
                var x = t.first!!
                var y = t.second!!
                if (x in minPos..maxPos && y in minPos..maxPos) {
                    if( (Math.abs(h1.x - x) > Math.abs(h1.x+h1.dx - x) && Math.abs(h2.x - x) > Math.abs(h2.x+h2.dx - x)) ) {
                        sum++
                    }
                }
            }
        }
    }
    println(sum/2)
}

// Part 2

fun part2() {
    var N: Int = 0
    var start: Int = 0
    while(true) {
        for (t in (start..N)) {
            var X = t.toDouble()
            var Y = N-X
            for(nX in listOf(-1, 1)) {
                for(nY in listOf(-1, 1)) {
                    var aX: Double = X*nX
                    var aY: Double = Y*nY
                    var h1 = hailStones[0]
                    h1.dx -= aX - h1.ax
                    h1.dy -= aY - h1.ay
                    h1.ax = aX
                    h1.ay = aY
                    var inter: Pair<Double?, Double?> = Pair(null,null)
                    var p: Pair<Double?, Double?> = Pair(null,null)
                    for(h2 in hailStones.drop(1)) {
                        h2.dx -= aX - h2.ax
                        h2.dy -= aY - h2.ay
                        h2.ax = aX
                        h2.ay = aY
                        p = intersect(h1, h2)
                        if (!nonNull(p)) {
                            break
                        } 
                        if (!nonNull(inter)) {
                            inter = p
                            continue
                        }
                        if (p != inter) {
                            break
                        }
                    }
                    if(!nonNull(p) || p != inter) {
                        continue
                    }
                    var aZ: Double? = null
                    var nZ: Double? = null
                    h1 = hailStones[0]
                    for(h2 in hailStones.drop(1)) {
                        nZ = getZ(h1, h2, inter)
                        if(aZ == null) {
                            aZ = nZ
                            continue
                        } else if (nZ != aZ) {
                            println("Invalid!!")
                            return
                        }
                    }
                    if (aZ == nZ) {
                        var h = hailStones[0]
                        var Z = h.z + getT(h, inter) * (h.dz - aZ!!)
                        println("found start pos: " + inter.first + ", " + inter.second + ", " + Z)
                        println(Z+inter.first!!+inter.second!!)
                        return
                    }
                }
            }
        }
        N++
    }
}

// Helper

fun intersect(h1: Hail, h2: Hail): Pair<Double?, Double?> {
    var m1 = h1.dy / h1.dx
    var b1 = h1.y - (m1*h1.x)
    var l1 = Line(m1, b1)

    var m2 = h2.dy / h2.dx
    var b2 = h2.y - (m2*h2.x)
    var l2 = Line(m2, b2)

    if(l1.m == l2.m){
        // Paralell lines don't intersect
        return Pair(null, null)
    }
    var x: Double
    var y: Double
    if((Double.POSITIVE_INFINITY).equals(l1.m) || (Double.NEGATIVE_INFINITY).equals(l1.m)) {
        x = h1.x
        y = l2.m * (x - h2.x) + h2.y
    } else if((Double.POSITIVE_INFINITY).equals(l2.m) || (Double.NEGATIVE_INFINITY).equals(l2.m)) {
        x = h2.x
        y = l1.m * (x - h1.x) + h1.y
    } else {
        x = (l2.b - l1.b) / (l1.m - l2.m);
        y = l1.m * x + l1.b;
    }

    return Pair(x.round(1),y.round(1));
}

fun getZ(h1: Hail, h2: Hail, inter: Pair<Double?, Double?>): Double? {
    var h1t = getT(h1, inter)
    var h2t = getT(h2, inter)
    if(h1t == h2t) {
        return null
    }
    return (h1.z - h2.z + (h1t*h1.dz) - (h2t*h2.dz))/(h1t - h2t)
}

fun getT(h1: Hail, inter: Pair<Double?, Double?>): Double {
    if(h1.dx == 0.0) {
        return (inter.second!! - h1.y)/h1.dy
    }
    return (inter.first!! - h1.x)/h1.dx
}

fun nonNull(p: Pair<Double?, Double?>): Boolean {
    return p.first != null && p.second != null && !Double.NaN.equals(p.first) && !Double.NaN.equals(p.second)
}

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()