package org.congnguyen.taskorganiser.web.models.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Graph<T> {
    @Getter
    private List<Node<T>> nodes = new ArrayList<>();

    @Getter
    private List<Edge> edges = new ArrayList<>();
}
