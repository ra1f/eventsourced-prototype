package common;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public enum FeelingOfSatiety {
  full, hungry, starving, dead;

  public FeelingOfSatiety next(Command command) {
    FeelingOfSatiety retVal = null;
    switch (command) {
      case Digest:
        if (this.equals(full)) retVal = hungry;
        else if (this.equals(hungry)) retVal = starving;
        else retVal = dead;
        break;
      case Feed:
        if (this.equals(dead)) retVal = dead;
        if (this.equals(starving)) retVal = hungry;
        if (this.equals(hungry)) retVal = full;
        else retVal = full;
        break;
      default:
        retVal = this;
    }
    return retVal;
  }
}
