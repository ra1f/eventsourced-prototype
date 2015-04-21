package zoo.states;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public enum Hygiene {
  tidy, smelly, filthy;

  public Hygiene better() {
    Hygiene hygiene;
    if (this.equals(filthy)) hygiene = smelly;
    else if (this.equals(smelly)) hygiene = tidy;
    else hygiene = tidy;
    return hygiene;
  }

  public Hygiene worse() {
    Hygiene hygiene;
    if (this.equals(tidy)) hygiene = smelly;
    else if (this.equals(smelly)) hygiene = filthy;
    else hygiene = filthy;
    return hygiene;
  }

  public boolean isWorst() {
    if (this.equals(filthy)) {
      return true;
    }
    return false;
  }
}
