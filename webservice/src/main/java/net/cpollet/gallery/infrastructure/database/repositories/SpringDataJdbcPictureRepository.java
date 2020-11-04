package net.cpollet.gallery.infrastructure.database.repositories;

import net.cpollet.gallery.infrastructure.database.entities.DBPicture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJdbcPictureRepository extends CrudRepository<DBPicture, Long> {
}
