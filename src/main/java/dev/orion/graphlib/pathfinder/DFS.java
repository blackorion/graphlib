package dev.orion.graphlib.pathfinder;

import dev.orion.graphlib.Edge;
import dev.orion.graphlib.Vertex;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
public class DFS<T> {

    Set<Edge<T>> edges;
    final List<Vertex<T>> NO_PATH_FOUND = Collections.emptyList();

    public List<Vertex<T>> find(Vertex<T> from, Vertex<T> to) {
        Map<Vertex<T>, Vertex<T>> round = walkThrough(from, to);

        return generatePath(to, round);
    }

    private Map<Vertex<T>, Vertex<T>> walkThrough(Vertex<T> from, Vertex<T> to) {
        Deque<Vertex<T>> queue = new ArrayDeque<>();
        Set<Vertex<T>> visited = new HashSet<>();
        Map<Vertex<T>, Vertex<T>> visitingRound = new HashMap<>();

        queue.add(from);

        while (!queue.isEmpty()) {
            Vertex<T> cur = queue.pop();
            visited.add(cur);

            if (cur.equals(to))
                return visitingRound;

            neighboursStreamOf(cur)
                    .filter(n -> !visited.contains(n))
                    .forEach(v -> {
                        visitingRound.put(v, cur);
                        queue.add(v);
                    });
        }

        return visitingRound;
    }

    private Stream<Vertex<T>> neighboursStreamOf(Vertex<T> cur) {
        return edges.stream()
                .filter(edge -> edge.getFrom().equals(cur))
                .map(Edge::getTo);
    }

    public List<Vertex<T>> generatePath(Vertex<T> to, Map<Vertex<T>, Vertex<T>> round) {
        if (!round.containsKey(to))
            return NO_PATH_FOUND;

        List<Vertex<T>> path = new ArrayList<>();

        while (to != null) {
            path.add(to);
            to = round.get(to);
        }

        Collections.reverse(path);

        return path;
    }

}
