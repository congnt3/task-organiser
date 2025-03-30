package org.congnguyen.taskorganiser.web.models;

import lombok.Data;

import java.util.List;

@Data
public class QueryRequestBody {
    private List<String> taskStatus;
}
