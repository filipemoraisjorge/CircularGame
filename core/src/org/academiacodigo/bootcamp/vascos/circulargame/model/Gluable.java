package org.academiacodigo.bootcamp.vascos.circulargame.model;

/**
 * Created by JVasconcelos on 16/03/16
 */
public interface Gluable {

    void glueTo(Gluable otherGluable);

    void touched(Gluable gluable);
}
