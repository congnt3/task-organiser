package org.congnguyen.taskorganiser.persistence.repositories;

import org.congnguyen.taskorganiser.persistence.models.ExternalLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalLinkRepository extends CrudRepository<ExternalLink, String> {
}
