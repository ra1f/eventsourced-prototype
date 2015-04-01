package zoo.common;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public enum Hygiene {
  tidy, smelly, filthy, dead;

  public Hygiene next(Command command) {
    Hygiene retVal = this;
    switch (command) {
      case MessUp:
        if (this.equals(tidy)) retVal = smelly;
        else if (this.equals(smelly)) retVal = filthy;
        else retVal = dead;
        break;
      case CleanUp:
        if (this.equals(dead)) retVal = dead;
        if (this.equals(filthy)) retVal = smelly;
        if (this.equals(smelly)) retVal = tidy;
        else retVal = tidy;
    }
    return retVal;
  }
}
