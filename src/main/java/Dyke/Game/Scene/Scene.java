package Dyke.Game.Scene;

import Dyke.GameObject.GameObject;
import Dyke.renderer.Camera;
import Dyke.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    boolean isRunning = false;
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

        if(isRunning == true){
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

    public abstract void update(float dt);
}
