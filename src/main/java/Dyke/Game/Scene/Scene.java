package Dyke.Game.Scene;

import Dyke.renderer.Camera;

public abstract class Scene {
    protected Camera camera;
    public Scene(){

    }

    public void init(){

    }

    public abstract void update(float dt);
}
