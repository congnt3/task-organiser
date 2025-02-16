package org.congnguyen.taskorganiser.domain.models;

import java.util.List;

public interface ITask {
    String getCode();
    void setCode(String code);

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    String getStatus();
    void setStatus(String status);

    List<IExternalLink> getExternalLinks();
    void setExternalLinks(List<IExternalLink> externalLinks);
}
