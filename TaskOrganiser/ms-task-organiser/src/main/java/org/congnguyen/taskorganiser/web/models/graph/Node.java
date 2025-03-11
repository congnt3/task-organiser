package org.congnguyen.taskorganiser.web.models.graph;

import lombok.Data;

@Data
public class Node<T> {
    T data;
    private String id;
    private String label;
    private Position position;
    private int depLevel;
}
