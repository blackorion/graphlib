package dev.orion.graphlib

import dev.orion.graphlib.Graph
import spock.lang.Specification

/*
Simple Graph lib:

Should support 2 types of graphs - directed and undirected with 3 operations:

 addVertex - adds vertex to the graph

 addEdge - adds edge to the graph

 getPath - returns a list of edges between 2 vertices (path doesn’t have to be optimal)

 Vertices should be of a user defined type.

 Questions to be ready to answer (don’t have to implement):

 Add weighted edges support in your lib.

Add traverse function that will take a user defined function and apply it on every vertex of the graph.

Make you graphs thread safe.

*/

class GraphSpec extends Specification {

    Graph<Person> persons = Graph.directed()
    Person john = new Person(name: "john")
    Person mike = new Person(name: "mike")
    Person vince = new Person(name: "vince")

    def 'should ignore re-added vertices'() {
        when:
        def a = persons.addVertex(john)
        def b = persons.addVertex(john)

        then:
        a == b
    }

    def 'should fail when linking unknown vertex'() {
        given:
        def validVx = persons.addVertex(john)
        def unknownVx = Graph.<Person> directed()
                .addVertex(new Person(name: "unknown"))

        when:
        persons.addEdge(validVx, unknownVx)
        persons.addEdge(unknownVx, validVx)

        then:
        thrown(Exception)
    }

    def 'should return empty array when no path available'() {
        given:
        def j = persons.addVertex(john)
        def m = persons.addVertex(mike)
        def v = persons.addVertex(vince)
        persons.addEdge(j, m)

        when:
        def path = persons.findPath(j, v)

        then:
        path.isEmpty()
    }

    def 'should find path'() {
        given:
        def j = persons.addVertex(john)
        def m = persons.addVertex(mike)
        def v = persons.addVertex(vince)
        persons.addEdge(j, v)
        persons.addEdge(v, m)

        when:
        def path = persons.findPath(j, m)

        then:
        path.first() == j
        path.last() == m
    }

    def 'should support undirected connections'() {
        given:
        def undirected = Graph.undirected()
        def j = undirected.addVertex(john)
        def m = undirected.addVertex(mike)
        undirected.addEdge(j, m)

        when:
        def path = undirected.findPath(m, j)

        then:
        path.first() == m
        path.last() == j
    }

    def 'should fail when adding null object'() {
        when:
        persons.addVertex(null)

        then:
        thrown(Exception)
    }

    class Person {
        String name
    }

}
