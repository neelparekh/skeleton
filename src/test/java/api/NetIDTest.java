package api;

import controllers.NetIDController;
import org.junit.Assert;
import org.junit.Test;

public class NetIDTest {

    @Test
    public void testValid() {
        NetIDController nidController = new NetIDController();

        Assert.assertEquals("np423", nidController.getNetID());
    }
}
