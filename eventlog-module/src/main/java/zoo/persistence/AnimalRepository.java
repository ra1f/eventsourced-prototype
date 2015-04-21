package zoo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public interface AnimalRepository extends JpaRepository<Animal, String> {
}
