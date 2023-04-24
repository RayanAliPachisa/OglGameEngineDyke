package Dyke.GameObject;

import Dyke.GameObject.Components.Component;
import Dyke.GameObject.Components.SpriteRenderer;
import Dyke.GameObject.Components.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    public Transform transform;
    private List<Component> components;
    public String name;

    public GameObject(String name){
        init(name, new Vector2f(),new Vector2f());
    }

    public GameObject(String name, Vector2f position){
        init(name, position, new Vector2f());
    }
    public GameObject(String name, Vector2f position,Vector2f scale){
        init(name, position, scale);
    }

    public void init(String name, Vector2f position, Vector2f scale){
        this.name = name;
        transform = new Transform(this);
        transform.transform(position, scale);
        components = new ArrayList<>();
    }

    public void onStart(){

    }

    public void update(float dt){

    }


    public <T> T getComponent(Class<T> classNeeded){
        for (Component component:components){
            if (component.getClass() == classNeeded){
                return (T) component;
            }
        }
        return null;
    }

    public void addComponent(Component component){
        component.parent = this;
        components.add(component);
    }

}
