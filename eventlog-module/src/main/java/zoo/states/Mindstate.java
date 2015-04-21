package zoo.states;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public enum Mindstate {
  happy, moody, boredOut;

  public Mindstate better() {
    Mindstate mindstate;
    if (this.equals(boredOut)) mindstate = moody;
    else if (this.equals(moody)) mindstate = happy;
    else mindstate = happy;
    return mindstate;
  }

  public Mindstate worse() {
    Mindstate mindstate;
    if (this.equals(happy)) mindstate = moody;
    else if (this.equals(moody)) mindstate = boredOut;
    else mindstate = boredOut;
    return mindstate;
  }

  public boolean isWorst() {
    if (this.equals(boredOut)) {
      return true;
    }
    return false;
  }
}
