package common;

/**
 * Created by dueerkopra on 27.03.2015.
 */
public enum Event {
  Digested, Fed, Saddened, Played, MessedUp, CleanedUp;

  public static Event fromCommand(Command command) {
    Event event = null;
    switch (command) {
      case Digest:
        event = Digested;
        break;
      case Feed:
        event = Fed;
        break;
      case Sadden:
        event = Saddened;
        break;
      case Play:
        event = Played;
        break;
      case MessUp:
        event = MessedUp;
        break;
      case CleanUp:
        event = CleanedUp;
        break;
    }
    return event;
  }
}
