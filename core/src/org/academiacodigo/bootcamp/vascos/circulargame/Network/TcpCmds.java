package org.academiacodigo.bootcamp.vascos.circulargame.Network;

/**
 * Created by filipejorge on 14/03/16.
 */
public enum TcpCmds {
    YOUR_TURN,
    MY_VELOCITY;


    public void send(TcpConnection connection, Object value) {
        connection.send(this.name() + " " + value);
    }


}
