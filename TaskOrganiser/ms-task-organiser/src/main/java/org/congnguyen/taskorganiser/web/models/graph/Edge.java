package org.congnguyen.taskorganiser.web.models.graph;

import lombok.Data;

@Data
public class Edge {
    private String id;
    private String source;
    private String target;
    private boolean animated = true;
}
