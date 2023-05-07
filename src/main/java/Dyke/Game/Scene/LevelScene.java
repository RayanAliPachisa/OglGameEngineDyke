package Dyke.Game.Scene;

import Dyke.GameObject.Components.Graphical.*;
import Dyke.GameObject.GameObject;
import Dyke.Input.KeyListener;
import Dyke.renderer.Camera;
import Dyke.renderer.Shader;
import Dyke.renderer.Texture;
import Dyke.util.AssetPool;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

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
        loadResources();
        this.camera = new Camera(new Vector2f());
        GameObject gb = new GameObject("Hi!", new Vector2f(500,500), new Vector2f(256,256));
        gb.addComponent(new SpriteRenderer(new Vector4f(1,0,1,1)));
        addGameObjectToScene(gb);
        this.activeGameObject = gameObjects.get(0);
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
    }

    @Override
    public void imgui() {
        ImGui.begin("Test Window");
        ImGui.text("L bozo");
        ImGui.end();
    }
}
