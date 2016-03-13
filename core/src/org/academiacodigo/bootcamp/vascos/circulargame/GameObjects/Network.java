package org.academiacodigo.bootcamp.vascos.circulargame.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.IOException;
import java.net.*;

/**
 * Created by filipejorge on 13/03/16.
 */
public class Network {

    private final String QUESTION = "CG_SOME1THR";
    private final String ANSWER = "CG_IM_HR";

    private boolean connected;

    private int hostPort = 49152 + (int) (Math.random() * (49152 - 65535)); //49152–65535
    private int destPort;

    private InetAddress clientAddress;
    private Socket clientSocket;


    public Network() {

        Thread listenerThread = new Thread(new UDPListener());
        listenerThread.setName("UDPListener");
        listenerThread.start();

        Thread broadcastThread = new Thread(new UDPBroadcast());
        broadcastThread.setName("UDPBroadcast");
        broadcastThread.start();

        if (connected) {
            SocketHints hints = new SocketHints();
            clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, clientAddress.getHostName(), destPort, hints);
        }


    }

    private class serverListener implements Runnable {
        @Override
        public void run() {
            //to tweak settings, like buffer size
            ServerSocketHints serverSocketHints = new ServerSocketHints();

            ServerSocket server = Gdx.net.newServerSocket(Net.Protocol.TCP, 55555, serverSocketHints);

            SocketHints hints = new SocketHints();
            //waiting to client connection;
            clientSocket = server.accept(hints);
        }
    }

    private class UDPBroadcast implements Runnable {
        @Override
        public void run() {

            try {
                DatagramSocket broadcastSocket = new DatagramSocket();
                broadcastSocket.setBroadcast(true);

                byte[] broadcastData = QUESTION.getBytes();
                InetAddress address = InetAddress.getByName("255.255.255.255"); //read that most routers block this. 192.168.0.255 should be better but inflexible.

                DatagramPacket isSomeoneThere = new DatagramPacket(broadcastData, broadcastData.length, address, 55555);

                while (!connected) {
                    broadcastSocket.send(isSomeoneThere);
                    Thread.sleep(1000);
                }

                broadcastSocket.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private class UDPListener implements Runnable {

        final int BUFFER_SIZE = 24;

        @Override
        public void run() {


            try {
                //Listen to all the UDP traffic that is destined for this port
                DatagramSocket listeningSocket;
                listeningSocket = new DatagramSocket(55555, InetAddress.getByName("0.0.0.0"));
                listeningSocket.setBroadcast(true);

                //Receive a packet
                byte[] receivedBuffer = new byte[BUFFER_SIZE];
                DatagramPacket imSomeone = new DatagramPacket(receivedBuffer, receivedBuffer.length);

                do {

                    listeningSocket.receive(imSomeone);
                    System.out.println(imSomeone.getAddress() + " " + listeningSocket +" " + imSomeone.getPort() +" " +hostPort);
                } while (imSomeone.getPort() == hostPort);

                //Packet received
                System.out.println("l received from: " + imSomeone.getAddress().getHostAddress() + " " + imSomeone.getPort());
                System.out.println("l data: " + new String(imSomeone.getData()).trim());

                //See if the packet holds the right command (message)
                String message = new String(imSomeone.getData()).trim();

                if (message.equals(QUESTION)) {
                    byte[] sendData = ANSWER.getBytes();
                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, imSomeone.getAddress(), imSomeone.getPort());
                    listeningSocket.send(sendPacket);

                    System.out.println("l responded to: " + sendPacket.getAddress().getHostAddress() + " " + sendPacket.getPort());

                    //set up the permanent connection- from this side
                    System.out.println("list " + imSomeone.getAddress().getHostAddress() + " " + imSomeone.getPort() + " " + imSomeone.getSocketAddress());
                    clientAddress = imSomeone.getAddress();
                    destPort = imSomeone.getPort();
                    hostPort = listeningSocket.getLocalPort();
                }

                listeningSocket.close();
                connected = true;

                Thread tcpServer = new Thread(new serverListener());
                tcpServer.start();

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}



