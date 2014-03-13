package testing.executable;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestMobileObject.class, TestCorrections.class, TestRateOfChangePredictor.class, TestWorld.class, TestZone.class })
public class TestWorldSuite {

}
