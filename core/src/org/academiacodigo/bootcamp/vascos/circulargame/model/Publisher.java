package org.academiacodigo.bootcamp.vascos.circulargame.model;

/**
 * Created by JVasconcelos on 16/03/16
 */
public interface Publisher {

    void registerSubscriber(Subscriber subscriber);
    void publish(Enum topic);

}
