package Dyke;

import Dyke.Game.Scene.LevelScene;
import Dyke.Game.Scene.Scene;
import Dyke.Input.KeyListener;
import Dyke.Input.MouseListener;
import Dyke.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow = 0l;

    private static Window window = null;

    private static int sceneIndex = -1;
    private static Scene currentScene = null;

    public static void changeScene(int newScene){
        sceneIndex = newScene;
        switch (newScene){
            case 0:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene!'" + newScene + "'";
                break;
        }
    }

    private Window(){
        this.width = 500;
        this.height = 500;
        this.title = "Dyke game engine";
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        init();
        loop();

        //Free the memory once loop exits
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init(){
        //Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to init GLFW");
        }

        //Config GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        //Create the window
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        //Setting mouse callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallback);

        //Setting key callbacks
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);

        //Making window visible
        glfwShowWindow(glfwWindow);

        //This line is critical for LWJGL's interoperation with GLFW's
        //OPENGL context, or any contect that is managed externally
        //LWJGL detects the context that is current in the current thread,
        //creates the GLCapabilities instance and makes the OpenGL bindings available for use
        GL.createCapabilities();


        //Making current scene
        changeScene(0);
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();

        while (!glfwWindowShouldClose(glfwWindow)){
            KeyListener.endFrame();
            MouseListener.endFrame();
            //Poll events
            glfwPollEvents();

            if(KeyListener.getKeyDown(GLFW_KEY_SPACE)){
                System.out.println("Space key was pressed!");
            }

            glClearColor(1f,1f,1f,1f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);

            if (Time.deltaTime >= 0){
                currentScene.update(Time.deltaTime);
            }

            endTime = Time.getTime();
            Time.deltaTime = endTime - beginTime;
            beginTime = endTime;
        }

    }
}