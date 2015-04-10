package zoo.states;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public enum FeelingOfSatiety {
  full, hungry, starving;

  public FeelingOfSatiety better() {
    FeelingOfSatiety satiety;
    if (this.equals(starving)) satiety = hungry;
    else if (this.equals(hungry)) satiety = full;
    else satiety = full;
    return satiety;
  }

  public FeelingOfSatiety worse() {
    FeelingOfSatiety satiety;
    if (this.equals(full)) satiety = hungry;
    else if (this.equals(hungry)) satiety = starving;
    else satiety = starving;
    return satiety;
  }

  public boolean isWorst() {
    if (this.equals(starving)) {
      return true;
    }
    return false;
  }
}
