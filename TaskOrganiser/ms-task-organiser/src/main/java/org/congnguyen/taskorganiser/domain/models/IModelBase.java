package org.congnguyen.taskorganiser.domain.models;

import java.time.LocalDateTime;

public interface IModelBase {
    LocalDateTime getCreatedDate();
    void setCreatedDate(LocalDateTime value);

    LocalDateTime getModifiedDate();
    void setModifiedDate(LocalDateTime value);
}
