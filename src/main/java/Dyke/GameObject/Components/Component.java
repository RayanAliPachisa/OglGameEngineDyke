package Dyke.GameObject.Components;

import Dyke.GameObject.GameObject;
import org.joml.Vector2f;

public abstract class Component {
    public GameObject parent;
    public Component(){

    }

    public abstract void start();
    public abstract void update(float dt);
}
