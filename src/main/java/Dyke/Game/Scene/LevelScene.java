package Dyke.Game.Scene;

import Dyke.GameObject.Components.*;
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
import java.util.Vector;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class LevelScene extends Scene{
    float timeSinceStart = 0f;
    float[] smootherStack = new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,0f};

    private Shader defualtShader = new Shader("Assets/Shaders/default.glsl");
    private Texture testTexture;
    private GameObject obj1;
    private  Spritesheet sprites;
    public LevelScene(){
        super();
        System.out.println("Hello Scene!");
    }


    @Override
    public void init(){
        //Calls to base class
        super.init();
        loadResources();
        this.camera = new Camera(new Vector2f());


        obj1 = new GameObject("Object 2", new Vector2f(128, 400), new Vector2f(256, 256), 1);
        obj1.addComponent(new SpriteRenderer(new Vector4f(1,0,0,0.4f)));
        this.addGameObjectToScene(obj1);

        sprites = AssetPool.getSpritesheet("spriteSheet.png", false);
        obj1 = new GameObject("Object 1", new Vector2f(0, 400), new Vector2f(256, 256), -1);
        obj1.addComponent(new SpriteRenderer(new Vector4f(0,0.8f,0,0.3f)));
        this.addGameObjectToScene(obj1);



        /*GameObject obj3 = new GameObject("Object 3", new Vector2f(10, 10), new Vector2f(256, 256));
        obj3.addComponent(new SpriteRenderer(new Sprite(sprites.sprites.get(2).getTexture(),sprites.sprites.get(2).getTexCoords())));
        this.addGameObjectToScene(obj3);

        GameObject obj4 = new GameObject("Object 4", new Vector2f(400, 10), new Vector2f(256, 256));
        obj4.addComponent(new SpriteRenderer(new Sprite(sprites.sprites.get(3).getTexture(),sprites.sprites.get(3).getTexCoords())));
        this.addGameObjectToScene(obj4);*/


    }

    private void loadResources(){
        AssetPool.getShader("default.glsl",false);

        AssetPool.addSpritesheet("obamaPrism.png",
                new Spritesheet(
                        AssetPool.getTexture("obamaPrism.png",false),
                        new SpritesheetLine(140, 84, 2, 20),
                        new SpritesheetLine(140, 84,2, 20)
                ),

                false

        );

        AssetPool.addSpritesheet("spriteSheet.png",
                new Spritesheet(
                        AssetPool.getTexture("spriteSheet.png",false),
                        16,16,14,2,0
                ),
                false
        );


    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

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


        /*spriteFlipTimeLeft -= dt;
        System.out.println(spriteIndex);
        if(spriteFlipTimeLeft <= 0){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if(spriteIndex > 4){
                spriteIndex = 0;
            }
            gameObjects.get(0).getComponent(SpriteRenderer.class).setSprite(sprites.sprites.get(spriteIndex));
        }

        gameObjects.get(0).transform.position.x += 100 * sdt;
        *//*System.out.println(obj1.transform.position.x);*/



        for (GameObject gameObject: gameObjects) {
            gameObject.update(dt);
        }
        this.renderer.render();
    }

}
