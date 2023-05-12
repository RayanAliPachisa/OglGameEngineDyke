package Dyke.GameObject.Components;

import Dyke.GameObject.Components.Physics.Rigidbody;
import Dyke.GameObject.Components.Physics.TransformLinkedFloat;
import Dyke.GameObject.GameObject;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Transform extends Component{
    public TransformLinkedFloat minX;
    public TransformLinkedFloat minY;
    public TransformLinkedFloat maxX;
    public TransformLinkedFloat maxY;
    public Vector2f position;
    public Vector2f scale;
    public Rigidbody rb;
    public ArrayList<TransformLinkedFloat> xColl;
    public ArrayList<TransformLinkedFloat> yColl;
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

    public Transform copy(){
        Transform t = new Transform(this.parent);
        t.position = new Vector2f(this.position);
        t.scale = new Vector2f(this.scale);
        return t;
    }

    public void copyTo(Transform to){
        to.scale = new Vector2f(this.scale);
        to.position = new Vector2f(this.position);
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof Transform)) return false;

        Transform t = (Transform) o;

        return (t.position.equals(this.position)) && (t.scale.equals(this.scale));
    }

}
