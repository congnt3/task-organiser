package org.congnguyen.taskorganiser.web.services;

import org.congnguyen.taskorganiser.web.models.graph.Graph;

public interface GraphOrderService<T> {
    Graph<T> markDepLevel(Graph<T> obj);
    Graph<T> markOrder(Graph<T> obj);
}
