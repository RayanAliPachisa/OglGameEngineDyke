package Dyke.Game.Scene;

import Dyke.GameObject.Components.Graphical.SpriteRenderer;
import Dyke.GameObject.Components.Physics.PhysicsManager;
import Dyke.GameObject.GameObject;
import Dyke.renderer.Camera;
import Dyke.renderer.Renderer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    boolean isRunning = false;
    protected GameObject activeGameObject = null;
    float[] smootherStack = new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,0f};
    protected Camera camera;
    List<GameObject> gameObjects;
    private PhysicsManager physicsManager;
    float sdt = 0f;
    public Scene(){
        gameObjects = new ArrayList<>();
    }

    public void init(){
        isRunning = true;
        for (GameObject gameObject: gameObjects) {
            gameObject.onStart();
            this.renderer.add(gameObject);
        }
        physicsManager = new PhysicsManager(this);
    }

    public void addGameObjectToScene(GameObject gameObject){
        physicsManager.registerGameObject(gameObject);
        gameObject.currentScene = this;
        if(isRunning){
            //Adding new game object to renderer
            this.renderer.add(gameObject);
            //Adding gameobject to list
            gameObjects.add(gameObject);
            gameObject.onStart();
        }else{
            gameObjects.add(gameObject);
        }

    }

    public void removeGameObjectFromScene(GameObject gameObject){
        SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
        spr.getRenderBatch().removeSprite(spr);
        this.gameObjects.remove(gameObject);
        //TODO Check out gameobject from physics manager
    }

    public Camera camera(){
        return this.camera;
    }

    public void update(float dt){
        for(int i = 0; i < smootherStack.length; i++){
            if(i != smootherStack.length - 1){
                smootherStack[i] = smootherStack[i+1];
            }else{
                smootherStack[i] = dt;
            }
        }

        for(float i:smootherStack){sdt += i;}
        sdt /= smootherStack.length;

        for (GameObject gameObject: gameObjects) {
            gameObject.update(dt);
        }
        this.renderer.render();
        physicsManager.update();
    }

    public void sceneImgui(){
        if(activeGameObject != null){
            activeGameObject.imgui();
        }

        imgui();
    }

    public void imgui(){

    }

    public Renderer getRenderer() {
        return renderer;
    }
}
