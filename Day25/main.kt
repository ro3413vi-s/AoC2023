import java.io.File

data class Connection(var comp1: String, var comp2: String)
var connections: MutableSet<Connection> = mutableSetOf()
var nodes: MutableSet<String> = mutableSetOf()

fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine{ 
        var t = it.split(": ") 
        var c1 = t[0]
        var c2 = t[1].split(' ')

        nodes.add(c1)
        nodes.addAll(c2)
        for(c in c2) {
            if(!(Connection(c,c1) in connections)) {
                connections.add(Connection(c1,c))
            }
        }
    }
    var t: Pair<MutableList<String>,MutableList<Connection>>
    do {
        var conns = mutableListOf<Connection>()
        for (con in connections) {
            // Need deep copy of the Connection elements
            conns.add(Connection(con.comp1, con.comp2))
        }
        t = Karger(nodes.toList().toMutableList(), conns)
    }while(t.second.size > 3)
    println("Answer: " + (t.first[0].split(',').size * t.first[1].split(',').size))
}

fun Karger(vertices: MutableList<String>, edges: MutableList<Connection>): 
        Pair<MutableList<String>,MutableList<Connection>> {
    while(vertices.size > 2) {
        var r = (0..edges.lastIndex).random()
        var e = edges[r]
        var newNode: String = e.comp1 + "," + e.comp2
        vertices.add(newNode)
        vertices.remove(e.comp1)
        vertices.remove(e.comp2)

        var removalEdges = mutableListOf<Connection>()
        for (edge in edges) {
            if(edge.comp1 in listOf(e.comp1, e.comp2) && edge.comp2 in listOf(e.comp1, e.comp2)){
                removalEdges.add(edge)
            } else if (edge.comp1 in listOf(e.comp1, e.comp2)) {
                edge.comp1 = newNode
            } else if(edge.comp2 in listOf(e.comp1, e.comp2)) {
                edge.comp2 = newNode
            }
        }
        for(edge in removalEdges){
            edges.remove(edge)
        }
    }
    return Pair(vertices, edges)
}