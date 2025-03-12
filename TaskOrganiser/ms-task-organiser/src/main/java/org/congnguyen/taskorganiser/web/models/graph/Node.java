package org.congnguyen.taskorganiser.web.models.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node<T> {
    private static final int MAX_CHILD_COUNT = 100;
    T data;
    private String id;
    private String label;
    private Position position = new Position();
    private int depLevel = -1;
    private String type = "default";

    @JsonIgnore
    private List<Node<T>> linked = new ArrayList<>();

    /**
     *
     * index to be used to sort for displaying on screen
     * calculated by:
     * - find p is this node's dependency at this.depLevel - 1 with smallest levelIndex
     * - MAX_CHILD_COUNT * (indexOf(p) within display Array of p.depLevel) + p.childCount++
     *
     */
    private int levelIndex = -1;

    private int dependantCount = 0;
}
