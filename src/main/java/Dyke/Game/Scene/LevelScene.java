package Dyke.Game.Scene;

import Dyke.GameObject.Components.Graphical.*;
import Dyke.GameObject.GameObject;
import Dyke.renderer.Camera;
import Dyke.renderer.Shader;
import Dyke.renderer.Texture;
import Dyke.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;


public class LevelScene extends Scene{
    float timeSinceStart = 0f;
    float[] smootherStack = new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,0f};

    private Shader defualtShader = new Shader("Assets/Shaders/default.glsl");
    private Texture testTexture;
    private GameObject obj1;
    private Spritesheet sprites;
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

        sprites = AssetPool.getSpritesheet("spriteSheet.png", false);
        obj1 = new GameObject("Game Object",new Vector2f(500,250),new Vector2f(256,256),5);
        obj1.addComponent(new SpriteRenderer(sprites.sprites.get(0)));



        //obj1.addComponent(new SpriteAnimation("Run",0.5f, sprites.getSublistOfSprites(0,3)));
        addGameObjectToScene(obj1);

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
