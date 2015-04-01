package zoo.common;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public enum Mindstate {
  happy, moody, bored, dead;

  public Mindstate next(Command command) {
    Mindstate retVal = this;
    switch (command) {
      case Sadden:
        if (this.equals(happy)) retVal = moody;
        else if (this.equals(moody)) retVal = bored;
        else retVal = dead;
        break;
      case Play:
        if (this.equals(dead)) retVal = dead;
        if (this.equals(bored)) retVal = moody;
        if (this.equals(moody)) retVal = happy;
        else retVal = happy;
    }
    return retVal;
  }
}
