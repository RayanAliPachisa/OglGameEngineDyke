package Dyke.GameObject.Components;

import Dyke.GameObject.GameObject;
import org.joml.Vector2f;

public class Transform extends Component{
    public Vector2f position;
    public Vector2f scale;
    public Transform(GameObject parent) {
        super();
        transform();
        this.parent = parent;
    }



    public void init(Vector2f position, Vector2f scale){
        this.position = position;
        this.scale = scale;
    }

    public void transform(){
        init(new Vector2f(), new Vector2f());
    }

    public void transform(Vector2f position){
        init(position, new Vector2f());
    }

    public void transform(Vector2f position, Vector2f scale){
        init(position, scale);
    }


    @Override
    public void start() {}

    @Override
    public void update(float dt) {}
}
