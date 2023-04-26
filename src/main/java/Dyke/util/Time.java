package Dyke.util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    public static float timeStarted = (float) glfwGetTime();

    public static float getTime(){return (float) glfwGetTime();} //Returns time since start of application in seconds

    public static float deltaTime = -1f;

}
