package net.cpollet.gallery.infrastructure.database.repositories;

import net.cpollet.gallery.infrastructure.database.entities.DBImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface SpringDataJdbcImageRepository extends CrudRepository<DBImage, Long> {
    Collection<DBImage> findByPictureId(Long id);
}
