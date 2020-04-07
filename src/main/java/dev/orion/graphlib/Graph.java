package dev.orion.graphlib;

import dev.orion.graphlib.pathfinder.DFS;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class Graph<T> {

    private final GraphType type;
    private final Set<Edge<T>> edges = Collections.synchronizedSet(new HashSet<>());
    private final Set<Vertex<T>> vertices = Collections.synchronizedSet(new HashSet<>());

    public Vertex<T> addVertex(@NonNull T person) {
        Vertex<T> vx = new Vertex<>(person);
        vertices.add(vx);

        return vx;
    }

    public void addEdge(Vertex<T> from, Vertex<T> to) {
        assertIsFamiliarWith(from);
        assertIsFamiliarWith(to);

        edges.addAll(type.createEdges(from, to));
    }

    private void assertIsFamiliarWith(Vertex<T> vertex) {
        if (!vertices.contains(vertex))
            throw new RuntimeException("unknown vertex:" + vertex);
    }

    public List<Vertex<T>> findPath(Vertex<T> from, Vertex<T> to) {
        Set<Edge<T>> edgesCopy = new HashSet<>(edges);

        return new DFS<>(edgesCopy).find(from, to);
    }

    public static <T> Graph<T> directed() {
        return new Graph<>(GraphType.DIRECTED);
    }

    public static <T> Graph<T> undirected() {
        return new Graph<>(GraphType.UNDIRECTED);
    }

    enum GraphType {

        DIRECTED {
            @Override
            <T> Collection<Edge<T>> createEdges(Vertex<T> from, Vertex<T> to) {
                return Collections.singletonList(new Edge<>(from, to));
            }
        },

        UNDIRECTED {
            @Override
            <T> Collection<Edge<T>> createEdges(Vertex<T> from, Vertex<T> to) {
                return Arrays.asList(
                        new Edge<>(from, to),
                        new Edge<>(to, from)
                );
            }
        };

        abstract <T> Collection<Edge<T>> createEdges(Vertex<T> from, Vertex<T> to);

    }

}