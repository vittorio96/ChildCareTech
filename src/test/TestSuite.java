import RMITest.RMITestSuite;
import RMITest.TestMensa.RmiModeTestMensa;
import SocketTest.SocketTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RMITestSuite.class,
        SocketTestSuite.class
})


public class TestSuite {
}
