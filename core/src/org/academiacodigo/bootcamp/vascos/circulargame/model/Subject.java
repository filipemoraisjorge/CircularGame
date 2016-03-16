package org.academiacodigo.bootcamp.vascos.circulargame.model;

/**
 * Created by JVasconcelos on 16/03/16
 */
public interface Subject {

    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();

}
