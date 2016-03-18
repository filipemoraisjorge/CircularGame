package org.academiacodigo.bootcamp.vascos.circulargame.model;

/**
 * Created by JVasconcelos on 16/03/16
 */
public interface Subscriber<T> {

    void update(Enum topic, T object);
}
