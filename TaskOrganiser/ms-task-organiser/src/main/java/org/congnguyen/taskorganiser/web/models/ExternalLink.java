package org.congnguyen.taskorganiser.web.models;

import lombok.Data;
import org.congnguyen.taskorganiser.domain.models.IExternalLink;

@Data
public class ExternalLink implements IExternalLink {

    private String code;

    private String name;

    private String description;

    private String url;
}
