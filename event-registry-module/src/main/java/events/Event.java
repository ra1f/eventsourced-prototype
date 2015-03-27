package events;

import commands.Command;

/**
 * Created by dueerkopra on 27.03.2015.
 */
public enum Event {
  Digested(Command.Digest),
  Fed(Command.Feed),
  Saddened(Command.Sadden),
  Played(Command.Play),
  MessedUp(Command.MessUp),
  CleanedUp(Command.CleanUp);

  private Command command;

  Event(Command command) { this.command = command; }
}
