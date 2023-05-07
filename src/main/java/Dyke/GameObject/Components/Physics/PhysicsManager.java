package Dyke.GameObject.Components.Physics;

import Dyke.Game.Scene.Scene;
import Dyke.GameObject.GameObject;

import java.util.ArrayList;

public class PhysicsManager {
    Scene currentScene;
    ArrayList<GameObject> gameObjects;

    public PhysicsManager(Scene currentScene) {
        this.currentScene = currentScene;
        this.gameObjects = new ArrayList<>();
    }

    public void registerGameObject(GameObject gameObject){
        gameObjects.add(gameObject);
    }

    public void update(){
        PhysicsUpdater physicsUpdater = new PhysicsUpdater();
        physicsUpdater.start();
    }
}
