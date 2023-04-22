package Dyke.Input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX,scrollY;
    private double xPos,yPos,lastY,lastX;
    //Button 1,2,3
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX = 0d;
        this.scrollY = 0d;
        this.xPos = 0d;
        this.yPos = 0d;
        this.lastX = 0d;
        this.lastY = 0d;
    }

    public static MouseListener get(){
        if(instance == null){
            instance = new MouseListener();
        }
        return instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos){
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;

        //TODO add dragging (blue-box) function later
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if (action == GLFW_PRESS){
            if (button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        }else if (button < get().mouseButtonPressed.length){
            get().mouseButtonPressed[button] = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx(){
        return (float) (get().lastX - get().xPos);
    }

    public static float getDY(){
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean getMouseDown(int button){
        if (button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }else{
            System.err.println("Int button out of range!");
            return false;
        }
    }


}
