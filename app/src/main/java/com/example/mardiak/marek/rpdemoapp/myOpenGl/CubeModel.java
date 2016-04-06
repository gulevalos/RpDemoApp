package com.example.mardiak.marek.rpdemoapp.myOpenGl;

/**
 * Created by mm on 3/23/2016.
 */
public class CubeModel implements MyModel{

    private volatile float mCubeRotationX = 0.2f;
    private volatile float mCubeRotationY = 0.2f;
    private volatile float mCubeRotationZ = 1f;


    public volatile float colorsCube[] = {
            0.3f, 0.2f, 1.0f, 1.0f
    };

    private final int coordsPerVertex = 3;

    private final float verticesCube[] = {
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f
    };

    {
        for (int i = 0; i < verticesCube.length; i++)
            verticesCube[i] = verticesCube[i] / 3;
    }

    private final short indicesCube[] = { //order of vertices
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 7, 6, 4, 6, 5,
            3, 0, 1, 3, 1, 2
    };


    @Override
    public int getCoordsPerVertex() {
        return coordsPerVertex;
    }

    @Override
    public float[] getCoordinates() {
        return verticesCube;
    }

    @Override
    public float[] getColor() {
        return colorsCube;
    }

    @Override
    public short[] drawOrder() {
        return indicesCube;
    }

    @Override
    public float getmCubeRotationX() {
        return mCubeRotationX;
    }

    @Override
    public float getmCubeRotationY() {
        return mCubeRotationY;
    }

    @Override
    public float getmCubeRotationZ() {
        return mCubeRotationZ;
    }
}
