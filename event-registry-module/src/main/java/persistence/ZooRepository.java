package persistence;

import common.AnimalId;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public interface ZooRepository extends CrudRepository<Zoo, AnimalId> {

}
