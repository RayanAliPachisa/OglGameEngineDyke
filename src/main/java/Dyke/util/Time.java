package Dyke.util;

public class Time {
    public static float timeStarted = System.nanoTime();

    public static float getTime(){return (float) ((float) (System.nanoTime() - timeStarted) * 1E-9);} //Returns time since start of application in seconds

    public static float deltaTime = -1f;

}
