package zoo.controllers;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class CommandResult {

  private boolean success;

  private long sequenceId;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public long getSequenceId() {
    return sequenceId;
  }

  public void setSequenceId(long sequenceId) {
    this.sequenceId = sequenceId;
  }
}
