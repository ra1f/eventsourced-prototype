package zoo.controllers;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class CommandResult {

  private Long sequenceId;

  public CommandResult(Long sequenceId) {
    this.sequenceId = sequenceId;
  }

  public long getSequenceId() {
    return sequenceId;
  }
}
