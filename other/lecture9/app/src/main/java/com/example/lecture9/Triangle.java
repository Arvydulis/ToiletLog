package com.example.lecture9;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {

    private FloatBuffer vertexBuffer;
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
            0.0f, 0.622008459f, 0.0f,
            -0.5f, -0.311004243f, 0.0f,
            0.5f, -0.311004243f, 0.0f
    };

    float color[] = {-0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    private final int Program;

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    " gl_Position = uMVPMatrix * vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int MVPMatrixHandle;

    private int PositionHandle;
    private int ColorHandle;

    private final int vertexCount = triangleCoords.length/ COORDS_PER_VERTEX;

    private final int vertexStride = COORDS_PER_VERTEX * 4;

    public Triangle() {

        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        int vertexShader = OpenGLRender.loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = OpenGLRender.loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);
        Program = GLES30.glCreateProgram();
        GLES30.glAttachShader(Program, vertexShader);
        GLES30.glAttachShader(Program, fragmentShader);
        GLES30.glLinkProgram(Program);
    }

    public void draw(float[] mvpMatrix){
        GLES30.glUseProgram(Program);
        PositionHandle = GLES30.glGetAttribLocation(Program, "vPosition");
        GLES30.glEnableVertexAttribArray(PositionHandle);
        GLES30.glVertexAttribPointer(PositionHandle, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, vertexStride, vertexBuffer);
        ColorHandle = GLES30.glGetUniformLocation(Program, "vColor");
        GLES30.glUniform4fv(ColorHandle, 1, color, 0);
        MVPMatrixHandle = GLES30.glGetUniformLocation(Program, "uMVPMatrix");
        GLES30.glUniformMatrix4fv(MVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, vertexCount);
        GLES30.glDisableVertexAttribArray(PositionHandle);
    }

}
