package zoo.states;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public enum FeelingOfSatiety {
  full, hungry, starving, dead;

  public FeelingOfSatiety better() {
    FeelingOfSatiety retVal;
    if (this.equals(dead)) retVal = dead;
    else if (this.equals(starving)) retVal = hungry;
    else if (this.equals(hungry)) retVal = full;
    else retVal = full;
    return retVal;
  }

  public FeelingOfSatiety worse() {
    FeelingOfSatiety retVal;
    if (this.equals(full)) retVal = hungry;
    else if (this.equals(hungry)) retVal = starving;
    else retVal = dead;
    return retVal;
  }
}
