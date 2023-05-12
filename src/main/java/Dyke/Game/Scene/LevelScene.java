package Dyke.Game.Scene;

import Dyke.GameObject.Components.Graphical.*;
import Dyke.GameObject.Components.Physics.PhysicsManager;
import Dyke.GameObject.Components.Physics.Rigidbody;
import Dyke.GameObject.GameObject;
import Dyke.Input.KeyListener;
import Dyke.renderer.Camera;
import Dyke.renderer.Shader;
import Dyke.renderer.Texture;
import Dyke.util.AssetPool;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;


public class LevelScene extends Scene{
    float timeSinceStart = 0f;

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
        Random rand = new Random();
        loadResources();
        this.camera = new Camera(new Vector2f());
        for (int i = 0; i < 50000; i++) {
            GameObject gb = new GameObject(Integer.toString(i),new Vector2f(rand.nextFloat() * 1000, rand.nextFloat() * 1000),new Vector2f(rand.nextFloat() * 10, rand.nextFloat() * 10), 0);
            gb.addComponent(new SpriteRenderer(new Vector4f(0,1,0,1)));
            gb.addComponent(new Rigidbody(gb));
            addGameObjectToScene(gb);
        }
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

        AssetPool.addSpritesheet("KlebV2-Sheet.png",
                new Spritesheet(
                        AssetPool.getTexture("KlebV2-Sheet.png",false),
                        85,85,30,1,14
                ),
                false
        );


    }

    @Override
    public void update(float dt) {
        super.update(dt);
        System.out.println(1/sdt);
    }

    @Override
    public void imgui() {
        ImGui.begin("Test Window");
        ImGui.text("L bozo");
        ImGui.end();
    }
}
