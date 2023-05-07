package Dyke.GameObject.Components.Physics;

import Dyke.GameObject.Components.Component;
import Dyke.GameObject.Components.Transform;
import Dyke.GameObject.GameObject;

public class Rigidbody extends Component{
    private Transform transform;
    private float velocityX;
    private float velocityY;
    private float accelerationX;
    private float accelarationY;

    public Rigidbody(GameObject parent) {
        this.parent = parent;
        velocityX = 0;
        velocityY = 0;
        accelarationY = 0;
        accelerationX = 0;
        transform = parent.getComponent(Transform.class);
    }


    @Override
    public void start() {

    }

    public void physicsUpdate(float deltaTime) {
        velocityX += accelerationX * deltaTime;
        velocityY += accelarationY * deltaTime;

        transform.position.x += velocityX;
        transform.position.y += velocityY;
    }
}
