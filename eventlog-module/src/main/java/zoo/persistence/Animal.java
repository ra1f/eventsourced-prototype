package zoo.persistence;

import zoo.states.FeelingOfSatiety;
import zoo.states.Hygiene;
import zoo.states.Mindstate;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@Entity
@Table(name="zoo")
public class Animal {

  @Id
  @Column(name="animal_id")
  private String animalId;

  @Column(name = "last_occurence")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastOccurence;

  @Column(name = "feeling_of_satiety")
  @Enumerated(EnumType.STRING)
  private FeelingOfSatiety feelingOfSatiety;

  @Column(name = "mindstate")
  @Enumerated(EnumType.STRING)
  private Mindstate mindstate;

  @Column(name = "hygiene")
  @Enumerated(EnumType.STRING)
  private Hygiene hygiene;

  @Version
  @Column(name = "optlock")
  private Integer version;

  /**
   * Convenience constructor
   * @param animalId
   * @param lastOccurence
   * @param feelingOfSatiety
   * @param mindstate
   * @param hygiene
   */
  public Animal(String animalId,
                Date lastOccurence,
                FeelingOfSatiety feelingOfSatiety,
                Mindstate mindstate,
                Hygiene hygiene) {
    this.animalId = animalId;
    this.lastOccurence = lastOccurence;
    this.feelingOfSatiety = feelingOfSatiety;
    this.mindstate = mindstate;
    this.hygiene = hygiene;
  }

  /**
   * Copy constructor
   * @param animalId
   * @param lastOccurence
   * @param feelingOfSatiety
   * @param mindstate
   * @param hygiene
   * @param version
   */
  public Animal(String animalId,
                Date lastOccurence,
                FeelingOfSatiety feelingOfSatiety,
                Mindstate mindstate,
                Hygiene hygiene,
                Integer version) {
    this.animalId = animalId;
    this.lastOccurence = lastOccurence;
    this.feelingOfSatiety = feelingOfSatiety;
    this.mindstate = mindstate;
    this.hygiene = hygiene;
    this.version = version;
  }

  /**
   * Hibernate constructor
   */
  protected Animal() {}

  public String getAnimalId() {
    return animalId;
  }

  public void setAnimalId(String animalId) {
    this.animalId = animalId;
  }

  public Date getLastOccurence() {
    return lastOccurence;
  }

  public void setLastOccurence(Date lastOccurence) {
    this.lastOccurence = lastOccurence;
  }

  public FeelingOfSatiety getFeelingOfSatiety() {
    return feelingOfSatiety;
  }

  public void setFeelingOfSatiety(FeelingOfSatiety feelingOfSatiety) {
    this.feelingOfSatiety = feelingOfSatiety;
  }

  public Mindstate getMindstate() {
    return mindstate;
  }

  public void setMindstate(Mindstate mindstate) {
    this.mindstate = mindstate;
  }

  public Hygiene getHygiene() {
    return hygiene;
  }

  public void setHygiene(Hygiene hygiene) {
    this.hygiene = hygiene;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Animal)) return false;

    Animal animal = (Animal) o;

    if (animalId != null ? !animalId.equals(animal.animalId) : animal.animalId != null) return false;
    if (feelingOfSatiety != animal.feelingOfSatiety) return false;
    if (hygiene != animal.hygiene) return false;
    if (lastOccurence != null ? !lastOccurence.equals(animal.lastOccurence) : animal.lastOccurence != null) return false;
    if (mindstate != animal.mindstate) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = animalId != null ? animalId.hashCode() : 0;
    result = 31 * result + (lastOccurence != null ? lastOccurence.hashCode() : 0);
    result = 31 * result + (feelingOfSatiety != null ? feelingOfSatiety.hashCode() : 0);
    result = 31 * result + (mindstate != null ? mindstate.hashCode() : 0);
    result = 31 * result + (hygiene != null ? hygiene.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return String.format(
        "Animal[animalId='%s', lastOccurence='%s', feelingOfSatiety='%s', mindstate='%s', hygiene='%s', version='%d']",
        animalId,
        lastOccurence == null? null: new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(lastOccurence),
        feelingOfSatiety,
        mindstate,
        hygiene,
        version);
  }
}
