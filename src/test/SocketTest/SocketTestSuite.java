package SocketTest;

import SocketTest.TestAnagrafica.SocketModeTestAnagrafica;
import SocketTest.TestGite.SocketModeTestGite;
import SocketTest.TestMensa.SocketModeTestMensa;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SocketModeTestAnagrafica.class,
        SocketModeTestGite.class,
        SocketModeTestMensa.class
})

public class SocketTestSuite {
}
