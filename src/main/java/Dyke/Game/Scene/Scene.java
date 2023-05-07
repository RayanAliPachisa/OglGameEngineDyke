package Dyke.Game.Scene;

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
    public Scene(){
        gameObjects = new ArrayList<>();
    }

    public void init(){
        isRunning = true;
        for (GameObject gameObject: gameObjects) {
            gameObject.onStart();
            this.renderer.add(gameObject);
        }
    }

    public void addGameObjectToScene(GameObject gameObject){

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
        float sdt = 0f;
        for(float i:smootherStack){sdt += i;}
        sdt /= smootherStack.length;

        for (GameObject gameObject: gameObjects) {
            gameObject.update(dt);
        }
        this.renderer.render();
    }

    public void sceneImgui(){
        if(activeGameObject != null){
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui(){

    }
}
