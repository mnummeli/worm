package fi.mikkonummelin.worm;

import org.apache.commons.logging.*;
import org.junit.*;

public class WormTest {

    private static final Log LOG = LogFactory.getLog(WormTest.class);

    @BeforeClass
    public static void setUp() {
        LOG.info("Starting tests.");
    }

    @Test
    public void trivialTest() {
        LOG.info("Entering trivial test.");
        Assert.assertEquals(2.0, 1.0 + 1.0, 0.01);
    }

    @AfterClass
    public static void tearDown() {
        LOG.info("Ending tests.");
    }
}
