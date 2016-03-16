package org.academiacodigo.bootcamp.vascos.circulargame.Network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.*;
import java.net.*;

/**
 * Created by filipejorge on 13/03/16.
 */
public class TcpConnection {

    private final String QUESTION = "CG_SOMEONE";
    private final String ANSWER =   "CG_LETS_GO";

    private int finderPort;
    private int broadcastPort;

    private boolean connected;

    private final int MAX_DELAY_TIME = 2000;
    private int randomDelayTime = 1000 + (int) (Math.random() * (MAX_DELAY_TIME - 1000)); // 500 - 1500

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public TcpConnection(int finderPort) {

        this.finderPort = finderPort;
        makeTCPConnection();
        initIOStreams();
    }

    public void send(String msg) {

        try {
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            System.out.println("S " + msg);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String receive() {
        String msg = null;
        try {
            msg = bufferedReader.readLine();

            System.out.println("R " + msg);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }

    private void initIOStreams() {
        bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }


    private void makeTCPConnection() {
        //Start UDP Listener on finderPort
        //If someone contacts I connect to it as TCPClient his IP+defaultServerPort
        Thread listenerThread = new Thread(new UDPListener());
        listenerThread.setName("UDPListener");
        listenerThread.start();

        //wait the maximum broadcastReplayTime
        //and check if I'm not connect already
        try {
            Thread.sleep(MAX_DELAY_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!connected) {
            //Lets broadcast so the other knows I'm here.
            makeUDPBroadcast(); //will cycle inside.
        }



    }


    private void makeUDPBroadcast() {

        try {
            DatagramSocket broadcastSocket = new DatagramSocket();
            broadcastSocket.setBroadcast(true);

            byte[] broadcastData = QUESTION.getBytes();
            InetAddress address = InetAddress.getByName("255.255.255.255"); //read that most routers block this. 192.168.0.255 should be better but inflexible.

            DatagramPacket isSomeoneThere = new DatagramPacket(broadcastData, broadcastData.length, address, finderPort);

            broadcastPort = broadcastSocket.getLocalPort();

            while (!connected) {
                System.out.println("broadcast to " + finderPort + " " + address + " from " + broadcastSocket.getLocalAddress() + " " + broadcastPort);
                broadcastSocket.send(isSomeoneThere);
                Thread.sleep(randomDelayTime);
            }
            //some one got our msg so lets end this.
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


    private class UDPListener implements Runnable {

        final int BUFFER_SIZE = 24;

        @Override
        public void run() {
            System.out.println("Listening");

            try {
                //Listen to all the UDP traffic that is destined for this port
                DatagramSocket listeningSocket;
                listeningSocket = new DatagramSocket(finderPort, InetAddress.getByName("0.0.0.0"));
                listeningSocket.setBroadcast(true);

                //Receive packet
                byte[] receivedBuffer = new byte[BUFFER_SIZE];
                DatagramPacket imSomeone = new DatagramPacket(receivedBuffer, receivedBuffer.length);
                do {
                    listeningSocket.receive(imSomeone);
                    //repeat if its from myself...
                } while (imSomeone.getPort() == broadcastPort);
                    System.out.println("l listen from " + imSomeone.getSocketAddress());

                //Packet received
                System.out.println("l listen msg: " + new String(imSomeone.getData()).trim());

                //See what message it is
                String message = new String(imSomeone.getData()).trim();

                //to tweak clientTCP
                SocketHints hints = new SocketHints();

                if (message.equals(QUESTION)) {

                    //warn that it can connect via UDP
                    byte[] connectData = ANSWER.getBytes();

                    DatagramPacket imHereLetsConnect = new DatagramPacket(connectData, connectData.length, imSomeone.getAddress(), finderPort);
                    listeningSocket.setBroadcast(false);
                    listeningSocket.send(imHereLetsConnect);
                    listeningSocket.close();

                    //turn TCP Server and wait for him.
                    //to tweak settings, like buffer size
                    ServerSocketHints serverSocketHints = new ServerSocketHints();

                    serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, imHereLetsConnect.getPort(), serverSocketHints);
                    System.out.println("serverSocket listening @" + imHereLetsConnect.getPort());

                    //waiting to client connection;
                    clientSocket = serverSocket.accept(hints);
                    System.out.println("serverSocket client login " + clientSocket.getRemoteAddress());

                    //Ok, I've connect to someone, lets warn that and naturally end this thread.
                    connected = true;

                }

                if (message.equals(ANSWER)) {
                    System.out.println("ANSWER routine " + imSomeone.getAddress().getHostAddress() + " " + imSomeone.getPort());
                    //wait the maximum broadcastReplayTime
                   /* try {
                        Thread.sleep(MAX_DELAY_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/

                    //connect as TCPClient
                    clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, imSomeone.getAddress().getHostAddress(), imSomeone.getPort(), hints);
                    //close UDP
                    listeningSocket.close();
                    connected = true;
                }

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



