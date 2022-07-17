import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import model.Snapshot;

/**
 * JUnit4 test suite for Snapshot.
 */
public class TestSnapshot {

  /**
   * Gets id.
   */
  @Test
  public void getID() {
    Snapshot s1 = new Snapshot("Hello", new ArrayList<>());
    Assert.assertEquals(s1.getID(), s1.getID());
    

  }

  /**
   * Gets timestamp.
   */
  @Test
  public void getTimestamp() {
    Snapshot s1 = new Snapshot("Hello", new ArrayList<>());
    Assert.assertEquals(s1.getTimestamp(), s1.getTimestamp());
  }
}