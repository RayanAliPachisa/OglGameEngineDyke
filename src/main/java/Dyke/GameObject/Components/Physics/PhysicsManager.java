package Dyke.GameObject.Components.Physics;

import Dyke.Game.Scene.Scene;
import Dyke.GameObject.Components.Graphical.SpriteRenderer;
import Dyke.GameObject.GameObject;
import org.joml.Vector4f;

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
        GameObject[] gameObjects1 = gameObjects.toArray(new GameObject[]{});
        physicsUpdater.prepare(gameObjects1);
        physicsUpdater.run();

        while (physicsUpdater.isAlive()){

        }

        ArrayList<Collision> collisions = physicsUpdater.collisions;

        for (Collision collision: collisions) {
            collision.transform1.parent.getComponent(SpriteRenderer.class).setColour(new Vector4f(1,0,0,1));
            collision.transform2.parent.getComponent(SpriteRenderer.class).setColour(new Vector4f(1,0,0,1));
        }
    }
}
