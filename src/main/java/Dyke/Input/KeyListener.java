package Dyke.Input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];
    private boolean spike[] = new boolean[350];

    private KeyListener(){

    }

    public static KeyListener get(){

        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }


    public static void keyCallback(long window, int key, int scancode, int action, int mods){
         if(key >= get().keyPressed.length){
             throw new RuntimeException("Key input out of range of program");
         }
        if (action == GLFW_PRESS){
            get().keyPressed[key] = true;
            get().spike[key] = true;
        }else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
            get().spike[key] = true;
        }
    }
    //If the key is pressed down in the current frame
    public static boolean isKeyPressed(int keyCode){
        if(keyCode >= get().keyPressed.length){
            throw new RuntimeException("Key input out of range of program");
        }
        return get().keyPressed[keyCode];
    }
    //If the key was released in the current frame
    public static boolean getKeyUp(int keyCode){
        if(keyCode >= get().keyPressed.length){
            throw new RuntimeException("Key input out of range of program");
        }
        return !(get().keyPressed[keyCode]) && get().spike[keyCode];
    }
    //If the key was pressed down in the current frame
    public static boolean getKeyDown(int keyCode){
        if(keyCode >= get().keyPressed.length){
            throw new RuntimeException("Key input out of range of program");
        }
        return get().keyPressed[keyCode] && get().spike[keyCode];
    }



    public static void endFrame(){
        get().spike = new boolean[350];
    }

}
