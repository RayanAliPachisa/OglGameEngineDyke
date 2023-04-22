package Dyke.Game.Scene;

public class LevelScene extends Scene{
    float timeSinceStart = 0f;


    public LevelScene(){
        System.out.println("Hello Scene!");
    }

    @Override
    public void update(float dt) {
        timeSinceStart += dt;
        System.out.println(timeSinceStart);

    }

}
