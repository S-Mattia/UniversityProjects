package com.example.client.support;

import Model.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * Create a Stream Socket and log the user in
 * @author: Sandri Mattia, Sandri Gabriele
 *
 */

public class clientCommunication {


    private Socket comunicationSocket;
    private int port = 6789;

    ObjectOutputStream out;
    ObjectInputStream in;

    /**
     * it allows the user to login by sending a request to the server
     * @param user the user that request for the information
     * @return a generic object sent by the server
     */
    public Object login(user user) throws IOException, ClassNotFoundException {
        connectServer();
        out.writeObject(new user(user.getEmail()));
        Object obj = in.readObject();
        disconnectServer();
        return obj;
    }


    /**
     * it creates a new Stream Socket, that use to open an input/output Object stream where to communicate
     */
    public void connectServer() throws IOException {
        System.out.println("trying to connect server...");
        comunicationSocket = new Socket(InetAddress.getLocalHost(), port);

        System.out.println("connected...");

        OutputStream outStream =comunicationSocket.getOutputStream();
        InputStream inStream = comunicationSocket.getInputStream();

        out = new ObjectOutputStream(outStream);
        in = new ObjectInputStream(inStream);
    }

    /**
    * it closes the input and output stream
    */
    public void disconnectServer() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
    }

    /**
     * Getter method for variable in
     * @return an ObjectInputStream where to read responses from the server
     */
    public ObjectInputStream getIn() {
        if (in != null){
            return in;
        }else{
            return null;
        }
    }

    /**
     * Getter method for variable out
     * @return an ObjectOutputStream where to write request to the server
     */
    public ObjectOutputStream getOut() {
        if (out != null){
            return out;
        }else{
            return null;
        }
    }

}
