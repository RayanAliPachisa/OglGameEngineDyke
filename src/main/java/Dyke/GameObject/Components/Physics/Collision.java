package Dyke.GameObject.Components.Physics;

import Dyke.GameObject.Components.Transform;

import java.util.Collections;

public class Collision {
    public Transform transform1, transform2;

    public Collision(Transform transform1, Transform transform2) {
        this.transform1 = transform1;
        this.transform2 = transform2;
    }
}
