package zoo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zoo.exceptions.AnimalNotFoundException;
import zoo.persistence.Animal;
import zoo.persistence.AnimalRepository;

/**
 * Created by dueerkopra on 14.04.2015.
 */
@RestController
public class ViewQueryController {

  @Autowired
  private AnimalRepository animalRepository;

  @RequestMapping(value = "/animals/{animalId}", method = RequestMethod.GET)
  public
  @ResponseBody
  Animal getAnimal(@PathVariable String animalId) {
    Animal animal = animalRepository.findOne(animalId);
    if (animal == null) {
      throw new AnimalNotFoundException();
    }
    return animal;
  }

  @RequestMapping(value = "/animals", method = RequestMethod.GET)
  public
  @ResponseBody
  Iterable<Animal> getAllAnimals() {
    Iterable<Animal> animals = animalRepository.findAll();
    if (animals == null) {
      throw new AnimalNotFoundException();
    }
    return animals;
  }

}
