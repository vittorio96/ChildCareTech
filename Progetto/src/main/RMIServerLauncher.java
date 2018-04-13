package main;

import java.rmi.Naming;

public class RMIServerLauncher {

    public static void main(String[] args) {
        try {
            //System.setSecurityManager(new RMISecurityManager());
            java.rmi.registry.LocateRegistry.createRegistry(1099);

            RemoteServerInterface server =new ServerRMI();
            Naming.rebind("rmi://127.0.0.1/stub", server);//Name binding
            System.out.println("[System] main.ServerRMI is ready.");
        }catch (Exception e) {
            System.out.println("main.ServerRMI failed: " + e);
        }
    }
}
