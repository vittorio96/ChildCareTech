package test.RMITest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RmiModeTestAnagrafica.class,
        RmiModeTestGite.class,
        RmiModeTestMensa.class
})

public class RMITestSuite {
}