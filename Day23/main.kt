import java.io.File

data class Node(var row: Int, var col: Int, var type: Char)
var graph: MutableSet<Node> = mutableSetOf()

data class Edge(var n1: Node, var n2: Node, var length: Int)
var edges: MutableSet<Edge> = mutableSetOf()

var lengths: MutableList<Int> = mutableListOf()

fun main() {
    var inFile = File("input.txt").readLines()
    inFile.forEachIndexed{ r, row -> row.forEachIndexed{ c, col ->
            if(col != '#') {
                graph.add(Node(r,c,col))
            }
        } 
    }
    var start: Node = graph.find{ it.row == 0 } ?: Node(-1,-1, ' ')
    var end: Node = graph.find{ it.row == inFile.size-1} ?: Node(-1,-1, ' ')
    DFSslopes(start,end,mutableSetOf<Node>())
    println(lengths.sortedBy{it})
    // Part 2
    lengths.clear()
    for(node in graph) {
        for(aNode in adjacentNodes(node)) {
            if(!edges.filter{it.n1 == aNode && it.n2 == node}.isEmpty()) continue
            edges.add(Edge(node, aNode, 1))
        }
    }
    for(node in graph) {
        var e = edges.filter{it.n1 == node || it.n2 == node}
        if(e.size == 2) {
            edges.remove(e[0])
            edges.remove(e[1])
            var node1 = if (e[0].n1 == node) e[0].n2 else e[0].n1
            var node2 = if (e[1].n1 == node) e[1].n2 else e[1].n1
            edges.add(Edge(node1, node2, e[0].length + e[1].length))
        }
    }
    DFS(start, end, 0, mutableSetOf<Node>())
    println(lengths.sortedBy{it})

}

fun DFSslopes(current: Node, end: Node, visited: MutableSet<Node>) {
    visited.add(current)
    if (current == end) {
        lengths.add(visited.size-1)
        visited.remove(current)
        return
    }
    if (current.type != '.' ) {
        when(current.type) {
            '<' -> {
                var t = graph.find{it.row == current.row && it.col == current.col-1}!!
                if (t in visited) return
                DFSslopes(t, end, visited)
            }
            '>' -> {
                var t = graph.find{it.row == current.row && it.col == current.col+1}!!
                if (t in visited) return
                DFSslopes(t, end, visited)
            }
            '^' -> {
                var t = graph.find{it.row == current.row-1 && it.col == current.col}!!
                if (t in visited) return
                DFSslopes(t, end, visited)
            }
            'v' -> {
                var t = graph.find{it.row == current.row+1 && it.col == current.col}!!
                if (t in visited) return
                DFSslopes(t, end, visited)
            }
        }
    } else {
        for(next in adjacentNodesSlopes(current)) {
            if (next in visited) continue
            DFSslopes(next, end, visited)
        }
    }
    visited.remove(current)
}

fun adjacentNodesSlopes(current: Node): List<Node> {
    return graph.filter {
        (it.row == current.row && it.col == current.col-1 && it.type in listOf('.', '<')) ||
        (it.row == current.row && it.col == current.col+1 && it.type in listOf('.', '>')) ||
        (it.col == current.col && it.row == current.row-1 && it.type in listOf('.', '^')) ||
        (it.col == current.col && it.row == current.row+1 && it.type in listOf('.', 'v'))
    }
}

// Part 2

fun DFS(current: Node, end: Node, steps: Int, visited: MutableSet<Node>) {
    visited.add(current)
    if (current == end) {
        lengths.add(steps)
        visited.remove(current)
        return
    }
    for( next in edges.filter{ it.n1 == current || it.n2 == current } ) {
        var t = if(next.n1 == current) next.n2 else next.n1
        if (t in visited) continue
        DFS(t, end, steps + next.length, visited)
    }
    visited.remove(current)
}

fun adjacentNodes(current: Node): List<Node> {
    return graph.filter {
        (it.row == current.row && it.col == current.col-1) ||
        (it.row == current.row && it.col == current.col+1) ||
        (it.col == current.col && it.row == current.row-1) ||
        (it.col == current.col && it.row == current.row+1)
    }
}