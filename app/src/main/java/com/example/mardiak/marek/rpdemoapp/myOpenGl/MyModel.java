package com.example.mardiak.marek.rpdemoapp.myOpenGl;

/**
 * Created by mm on 3/24/2016.
 */
public interface MyModel {
    int getCoordsPerVertex();
    float[] getCoordinates();
    float[] getColor();
    short[] drawOrder();

    float getmCubeRotationX();
    float getmCubeRotationY();
    float getmCubeRotationZ();
}
