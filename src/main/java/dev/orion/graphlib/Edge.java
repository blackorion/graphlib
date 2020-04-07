package dev.orion.graphlib;

import lombok.Value;

@Value
public class Edge<T> {
    Vertex<T> from;
    Vertex<T> to;
}
