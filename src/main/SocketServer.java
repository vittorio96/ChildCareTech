package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    private final static int PORT=1100;
    private ServerSocket serverSocket;


    public SocketServer(){

        System.out.println("SocketServer process started");

        try{
            serverSocket = new ServerSocket(PORT);
        }

        catch(IOException e){
            System.err.println(e.getMessage());
            return ;
        }

        this.waitConnection();

    }

    private void waitConnection() {

        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("[System] main.ServerSocket is ready.\"");

        while(true){

            try{
                Socket socket = serverSocket.accept(); //Attesa bloccante
                executor.submit(new ClientHandler(socket, this));
                //executor.submit(new ServerSocket_SQLIF(socket, this));
            }

            catch(IOException e){
                break;
            }
        }

        executor.shutdown();

        try{
            serverSocket.close();
        }

        catch(IOException e){
            System.err.println(e.getMessage());
            return ;
        }
    }


    public static void main(String[] args){

        SocketServer server = new SocketServer();

    }
}
