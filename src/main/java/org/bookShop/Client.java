package org.bookShop;//A basic java client using Sockets

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    //initialise instance variables used throughout class. We don't need them to be used
    // outside this class, so we make them private
    private Socket socket = null;
    private Scanner in = null;
    private PrintWriter out = null;
    private Scanner userInput = null;

    //constructor for Client class, takes an IP address and port as arguments
    public Client(String IPaddress, int port){
        //establish connection within try/catch
        try{
            //Attempts to make a connection at the given address and port
            socket = new Socket(IPaddress, port);
            System.out.println("Connected to server at "+ IPaddress + ":" + port);
            //Connect a Scanner to the socket's InputStream to read lines from it
            in = new Scanner(socket.getInputStream());
            //Connect a PrintWriter to the OutputStream to send lines to server
            out = new PrintWriter(socket.getOutputStream(), true);
            //Connect a Scanner to system input to take command line instructions
            System.out.println("Enter a command");
            System.out.println("Press Ctrl+C or Ctrl+D to quit");
            userInput = new Scanner(System.in);
            while(userInput.hasNext()){
                out.println(userInput.nextLine()); //handle output to server
                System.out.println(in.nextLine()); //print input from server
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        Client client = new Client("localhost", 54321); //could also use "127.0.0.1"
    }
}
