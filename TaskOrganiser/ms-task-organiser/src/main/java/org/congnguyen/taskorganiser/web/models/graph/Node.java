package org.congnguyen.taskorganiser.web.models.graph;

import lombok.Data;

@Data
public class Node<T> {
    T data;
    private String id;
    private String label;
    private Position position = new Position();
    private int depLevel;
    private String type = "default";
}
