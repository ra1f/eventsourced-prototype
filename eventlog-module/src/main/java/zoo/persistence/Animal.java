package zoo.persistence;

import zoo.states.FeelingOfSatiety;
import zoo.states.Hygiene;
import zoo.states.Mindstate;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@Entity
@Table(name="zoo")
public class Animal implements Serializable {

  @Id
  @Column(name="animal_id")
  private String animalId;

  @Column(name="seq_id")
  private Long sequenceId;

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
   * @param sequenceId
   * @param lastOccurence
   * @param feelingOfSatiety
   * @param mindstate
   * @param hygiene
   */
  public Animal(String animalId,
                Long sequenceId,
                Date lastOccurence,
                FeelingOfSatiety feelingOfSatiety,
                Mindstate mindstate,
                Hygiene hygiene) {
    this.animalId = animalId;
    this.sequenceId = sequenceId;
    this.lastOccurence = lastOccurence;
    this.feelingOfSatiety = feelingOfSatiety;
    this.mindstate = mindstate;
    this.hygiene = hygiene;
  }

  /**
   * Copy constructor
   * @param animalId
   * @param sequenceId
   * @param lastOccurence
   * @param feelingOfSatiety
   * @param mindstate
   * @param hygiene
   * @param version
   */
  public Animal(String animalId,
                Long sequenceId,
                Date lastOccurence,
                FeelingOfSatiety feelingOfSatiety,
                Mindstate mindstate,
                Hygiene hygiene,
                Integer version) {
    this.animalId = animalId;
    this.sequenceId = sequenceId;
    this.lastOccurence = lastOccurence;
    this.feelingOfSatiety = feelingOfSatiety;
    this.mindstate = mindstate;
    this.hygiene = hygiene;
    this.version = version;
  }

  /**
   * Hibernate constructor
   */
  public Animal() {}

  public String getAnimalId() {
    return animalId;
  }

  public Long getSequenceId() {
    return sequenceId;
  }

  public Date getLastOccurence() {
    return lastOccurence;
  }

  public FeelingOfSatiety getFeelingOfSatiety() {
    return feelingOfSatiety;
  }

  public Mindstate getMindstate() {
    return mindstate;
  }

  public Hygiene getHygiene() {
    return hygiene;
  }

  public Integer getVersion() {
    return version;
  }

  public void setAnimalId(String animalId) {
    this.animalId = animalId;
  }

  public void setSequenceId(Long sequenceId) {
    this.sequenceId = sequenceId;
  }

  public void setLastOccurence(Date lastOccurence) {
    this.lastOccurence = lastOccurence;
  }

  public void setFeelingOfSatiety(FeelingOfSatiety feelingOfSatiety) {
    this.feelingOfSatiety = feelingOfSatiety;
  }

  public void setMindstate(Mindstate mindstate) {
    this.mindstate = mindstate;
  }

  public void setHygiene(Hygiene hygiene) {
    this.hygiene = hygiene;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Animal)) return false;

    Animal animal = (Animal) o;

    if (!animalId.equals(animal.animalId)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return animalId.hashCode();
  }

  @Override
  public String toString() {
    return String.format(
        "Animal[animalId='%s', sequenceId='%d', lastOccurence='%s', feelingOfSatiety='%s', mindstate='%s', hygiene='%s', version='%d']",
        animalId,
        sequenceId,
        lastOccurence == null? null: new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(lastOccurence),
        feelingOfSatiety,
        mindstate,
        hygiene,
        version);
  }
}
