package test;

import test.RMITest.RMITestSuite;
import test.SocketTest.SocketTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RMITestSuite.class,
        SocketTestSuite.class
})


public class TestSuite {
}
