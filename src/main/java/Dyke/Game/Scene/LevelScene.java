package Dyke.Game.Scene;

import Dyke.GameObject.Components.SpriteRenderer;
import Dyke.GameObject.Components.Transform;
import Dyke.GameObject.GameObject;
import Dyke.renderer.Camera;
import Dyke.renderer.Shader;
import Dyke.renderer.Texture;
import Dyke.util.Time;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class LevelScene extends Scene{
    float timeSinceStart = 0f;
    float[] smootherStack = new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,0f};

    private Shader defualtShader = new Shader("Assets/Shaders/default.glsl");
    private Texture testTexture;

    public LevelScene(){
        super();
        System.out.println("Hello Scene!");
    }


    @Override
    public void init(){
        //Calls to base class
        super.init();
        this.camera = new Camera(new Vector2f());
        int yOffset = 10;
        int xOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float) (300 - yOffset * 2);
        float sizeX = totalWidth / 100f;
        float sizeY = totalHeight / 100f;

        for(int x = 0; x < 100; x++){
            for(int y = 0; y < 100; y++){
                float xPos = xOffset + (x*sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject gameObject = new GameObject("Obj" + x + y, new Vector2f(xPos, yPos),new Vector2f(sizeX, sizeY));
                gameObject.addComponent(new SpriteRenderer(new Vector4f(xPos/totalWidth,yPos/totalHeight,1,1)));
                this.addGameObjectToScene(gameObject);
            }
        }
    }

    @Override
    public void update(float dt) {
        for(int i = 0; i < smootherStack.length; i++){
            if(i != smootherStack.length - 1){
                smootherStack[i] = smootherStack[i+1];
            }else{
                smootherStack[i] = dt;
            }
        }
        float sdt = 0f;
        for(float i:smootherStack){sdt += i;}
        sdt /= smootherStack.length;

        for (GameObject gameObject: gameObjects) {
            gameObject.update(dt);
        }
        this.renderer.render();
    }

}
