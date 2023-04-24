package Dyke.GameObject.Components;

import Dyke.GameObject.GameObject;
import org.joml.Vector4f;

public class SpriteRenderer extends Component{
    public Vector4f colour;
    public SpriteRenderer(Vector4f colour) {
        super();
        this.colour = colour;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

}
