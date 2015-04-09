package zoo.states;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public enum Mindstate {
  happy, moody, bored, dead;

  public Mindstate better() {
    Mindstate mindstate;
    if (this.equals(dead)) mindstate = dead;
    else if (this.equals(bored)) mindstate = moody;
    else if (this.equals(moody)) mindstate = happy;
    else mindstate = happy;
    return mindstate;
  }

  public Mindstate worde() {
    Mindstate mindstate;
    if (this.equals(happy)) mindstate = moody;
    else if (this.equals(moody)) mindstate = bored;
    else mindstate = dead;
    return mindstate;
  }
}
