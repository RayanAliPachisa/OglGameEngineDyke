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
    private int zIndex = 0;

    public GameObject(String name){
        init(name, new Vector2f(),new Vector2f(), 0);
    }

    public GameObject(String name, Vector2f position){
        init(name, position, new Vector2f(), 0);
    }
    public GameObject(String name, Vector2f position,Vector2f scale){
        init(name, position, scale, 0);
    }
    public GameObject(String name, Vector2f position,Vector2f scale, int zIndex){
        init(name, position, scale, zIndex);
    }

    public void init(String name, Vector2f position, Vector2f scale, int zIndex){
        this.name = name;
        transform = new Transform(this);
        transform.transform(position, scale);
        components = new ArrayList<>();
        this.zIndex = zIndex;
    }

    public void onStart(){
        for (Component component: components) {
            component.start();
        }
    }

    public void update(float dt){
        for (Component component: components) {
            component.update(dt);
        }
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

    public int getZIndex() {
        return zIndex;
    }
}
