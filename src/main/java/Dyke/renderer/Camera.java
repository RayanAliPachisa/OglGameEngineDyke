package Dyke.renderer;


import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;


public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position){
        this.position = position;
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
        projectionMatrix.identity();

        projectionMatrix.ortho( 0.0f, 32.0f*40f,//40 grid tiles wide 32*32 pixels each
                                    0.0f,32.0f*21.0f,  //32 grid tiles tall 21 pixels each
                                    0.0f, 120.0f);
    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f,0.0f,-1.0f);
        Vector3f cameraUp = new Vector3f(0.0f,1.0f,0.0f);
        this.viewMatrix.identity();
        this.viewMatrix = viewMatrix.lookAt(new Vector3f(position.x,position.y,20f),
                cameraFront.add(position.x,position.y,0.0f),
                cameraUp);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }

}
