package org.bookShop;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.sql.*;

public class Server {
    //initialise instance variables
    private ServerSocket listener = null;
    private Socket socket = null;
    private Scanner in = null;
    private PrintWriter out = null;

    // constructor for server class, taking a port and a message for clients that connect
    public Server(int port) {
        try {
            listener = new ServerSocket(port); //create serversocket to listen for clients
            System.out.println("Listening on port " + port);
            //keep trying to listen for clients forever
            while (true) {
                socket = listener.accept(); //accept connection from client and establish socket
                System.out.println("Connected to client!");
                out = new PrintWriter(socket.getOutputStream(), true); //hook up PrintWriter to OutputStream of socket

                in = new Scanner(socket.getInputStream());

                while (in.hasNext()) {
                    String option = in.nextLine();

                    if (option.contains("BOOK ADD")) {
                        try (
                                //Create a connection
                                Connection conn = DriverManager.getConnection(
                                        "jdbc:mysql://localhost:3306/bookdb?useSSL=false",
                                        "root", "Password123");   // For MySQL only
                                // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

                                //Create a statement in the SQL connection
                                Statement statement = conn.createStatement();
                        ) {
                            // Build SQL statement
                            String[] data_split = option.split(" ");
                            String insert = "INSERT INTO `bookdb`.`book`" +
                                    "(`ISBN`," +
                                    "`title`," +
                                    "`author`," +
                                    "`publisher`," +
                                    "`language`)" +
                                    "VALUES" +
                                    "('" + data_split[2] + "'," +
                                    "'" + data_split[3] + "'," +
                                    "'" + data_split[4] + "'," +
                                    "'" + data_split[5] + "'," +
                                    "'" + data_split[6] + "');";

                            // Execute Query
                            statement.execute(insert);
                            out.println("Book Successfully added");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            out.println("Error");
                        }
                    } else if (option.contains("BOOK SEARCH")) {
                        try (
                                //Create a connection
                                Connection conn = DriverManager.getConnection(
                                        "jdbc:mysql://localhost:3306/bookdb?useSSL=false",
                                        "root", "Password123");   // For MySQL only
                                // Create a statement in the SQL connection
                                Statement statement = conn.createStatement();
                        ) {
                            String[] data_split = option.split(" ");
                            // Build SQL statement
                            String insert = "SELECT * FROM book WHERE title like '" + data_split[2] + "'";
                            // Execute Query
                            try (ResultSet rs = statement.executeQuery(insert);) {
                                if (rs.next()) {
                                    out.println("Book with title '" + rs.getString(5) + "' found");
                                } else {
                                    out.println("No book with title '" + data_split[2] + "' found");
                                }
                            } catch (SQLException e) {
                                out.println(e);
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                            out.println("Error");
                        }
                    } else {
                        out.println("Command Not Found");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(54321);
    }
}