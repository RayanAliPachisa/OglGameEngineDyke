package Dyke.Game.Scene;

import Dyke.GameObject.Components.SpriteRenderer;
import Dyke.GameObject.Components.Transform;
import Dyke.GameObject.GameObject;
import Dyke.renderer.Camera;
import Dyke.renderer.Shader;
import Dyke.renderer.Texture;
import Dyke.util.AssetPool;
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

        GameObject obj1 = new GameObject("Object 1", new Vector2f(100, 100), new Vector2f(256, 256));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("obamaPrism.png",false)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Vector2f(400, 400), new Vector2f(256, 256));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("obamaSphere.png",false)));
        this.addGameObjectToScene(obj2);

        loadResources();
    }

    private void loadResources(){
        AssetPool.getShader("default.glsl",false);
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
