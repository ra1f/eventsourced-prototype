package zoo.persistence;

import zoo.common.AnimalId;
import zoo.common.FeelingOfSatiety;
import zoo.common.Hygiene;
import zoo.common.Mindstate;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@Entity
@Table(name="zoo")
public class Zoo {

  @Id
  @Column(name="animal_id")
  @Enumerated(EnumType.STRING)
  private AnimalId animalId;

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

  public Zoo(AnimalId animalId, FeelingOfSatiety feelingOfSatiety, Mindstate mindstate, Hygiene hygiene) {
    this.animalId = animalId;
    this.feelingOfSatiety = feelingOfSatiety;
    this.mindstate = mindstate;
    this.hygiene = hygiene;
  }

  protected Zoo() {}

  public AnimalId getAnimalId() {
    return animalId;
  }

  public void setAnimalId(AnimalId animalId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Zoo)) return false;

    Zoo zoo = (Zoo) o;

    if (animalId != null ? !animalId.equals(zoo.animalId) : zoo.animalId != null) return false;
    if (feelingOfSatiety != zoo.feelingOfSatiety) return false;
    if (hygiene != zoo.hygiene) return false;
    if (lastOccurence != null ? !lastOccurence.equals(zoo.lastOccurence) : zoo.lastOccurence != null) return false;
    if (mindstate != zoo.mindstate) return false;

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
        "Zoo[animalId='%s', lastOccurence='%s', feelingOfSatiety='%s', mindstate='%s', hygiene='%s']",
        animalId,
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(lastOccurence),
        feelingOfSatiety,
        mindstate,
        hygiene);
  }
}
