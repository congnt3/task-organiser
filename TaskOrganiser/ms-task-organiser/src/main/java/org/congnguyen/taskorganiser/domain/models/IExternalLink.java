package org.congnguyen.taskorganiser.domain.models;

public interface IExternalLink {
    String getCode();
    void setCode(String code);

    String getUrl();
    void setUrl(String url);

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);
}
