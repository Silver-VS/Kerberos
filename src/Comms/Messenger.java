package Comms;

import dominio.Ticket.UTicket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Silver-VS
 */

public class Messenger {

    public static Socket initSocket(String receiverHost, int connectionPort) throws IOException {
        //  We indicate the destination of the Ticket, establishing the IP where it will be received and the
        //  "channel" or port where both all comms will be held.
        //  The socket indicated in here must be already running in the receiverHost, or the connection
        //  won't be established.
        return new Socket(receiverHost, connectionPort);
    }

    public static ObjectOutputStream initSender(Socket socket) throws IOException {
        //  We state that we are sending something through an outputStream.
        OutputStream outputStream = socket.getOutputStream();
        //  Now we clarify that we are sending an object through said stream.
        return new ObjectOutputStream(outputStream);
    }

    public UTicket sendTicket(String receiverHost, int connectionPort, UTicket ticket) {
        try {
            Socket socket = initSocket(receiverHost, connectionPort);
            //  Now we need to send the object through the connection.
            initSender(socket).writeObject(ticket);

            //  We show in the console what are we trying to send.
            System.out.print("\nTicket enviado:");
            ticket.printTicket(ticket);
            System.out.print("\ntermina ticket enviado");

            //  So now we think it has been sent, but we need to be sure of it.
            //  We are going to be receiving information from the socket to confirm
            //  the reception of the object.
            InputStream inputStream = socket.getInputStream();
            //  The server will be returning a boolean, which is already serialized, so we can make
            //  use of the already existing methods for sending and receiving booleans.
            ObjectInputStream objectReceiver = new ObjectInputStream(inputStream);
            //  At this point, we are reading the information sent as a response for our request.
            UTicket ticket1 = (UTicket) objectReceiver.readObject();

            System.out.println("Recibido en red:");
            ticket1.printTicket(ticket1);
            System.out.println("Termina recibo en red");

            //  Now that we have a response we can close the communication channel.
            socket.close();

            return ticket1;
        } catch (Exception e) {
            return null;
        }
    }

    public ServerSocket initServerSocket(int receiverPort) {
        //  A server socket takes a request and can send a response without the need to start a second socket.
        try {
            return new ServerSocket(receiverPort);
        } catch (IOException e) {
            return null;
        }
    }

    public Socket acceptRequest(ServerSocket serverSocket) {
        try {
            //  Now we will accept incoming messages from the established channel.
            return serverSocket.accept();
        } catch (IOException e) {
            return null;
        }
    }

    public Socket acceptRequest(int receiverPort) {
        return acceptRequest(initServerSocket(receiverPort));
    }

    public UTicket acceptTicket(Socket socket) {
        try {
            //  Once accepted, we are going to need to read the information received.
            InputStream inputStream = socket.getInputStream();
            //  We specify that we will be reading an object from said stream.
            ObjectInputStream objectReceiver = new ObjectInputStream(inputStream);
            //  Now we need to read the Ticket.
            return (UTicket) objectReceiver.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean ticketResponse(Socket socket, UTicket ticketResponse) {
        try {
            //  We send the response ticket.
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectSender = new ObjectOutputStream(outputStream);
            objectSender.writeObject(ticketResponse);

            //  We sout the ticket sent back
            ticketResponse.printTicket(ticketResponse);
            System.out.println("\n");

            //  We can proceed to close the receiving socket.
            socket.close();
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean returnResponse(Socket socket, boolean response) {
        try {
            //  We send the response ticket.
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectSender = new ObjectOutputStream(outputStream);
            objectSender.writeBoolean(response);
            //  We can proceed to close the receiving socket.
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
