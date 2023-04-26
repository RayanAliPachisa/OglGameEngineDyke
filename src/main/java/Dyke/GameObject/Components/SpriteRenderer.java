package Dyke.GameObject.Components;

import Dyke.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component{
    public Vector4f colour;
    private Vector2f[] texCoords;
    private Texture texture;

    public SpriteRenderer(Vector4f colour) {
        super();
        this.colour = colour;
        this.texture = null;
    }

    public SpriteRenderer(Texture texture) {
        super();
        this.texture = texture;
        this.colour = new Vector4f(1,1,1,1);
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexCoords() {
        Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

        return texCoords;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

}
