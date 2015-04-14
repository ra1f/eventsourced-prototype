package zoo.persistence;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public interface AnimalRepository extends CrudRepository<Animal, String> {
}
