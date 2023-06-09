package Dyke;

import Dyke.GUI.ImGuiLayer;
import Dyke.Game.Scene.LevelScene;
import Dyke.Game.Scene.Scene;
import Dyke.Input.KeyListener;
import Dyke.Input.MouseListener;
import Dyke.util.FileWindowHandler;
import Dyke.util.RunnableWithInvokeDelay;
import Dyke.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_MAX_TEXTURE_IMAGE_UNITS;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow = 0l;
    private ImGuiLayer imGuiLayer;

    private static Window window = null;

    private static int sceneIndex = -1;
    private static Scene currentScene = null;
    private ArrayList<RunnableWithInvokeDelay> functionsToInvoke = new ArrayList<>();
    private ArrayList<RunnableWithInvokeDelay> functionsToInvokeBuffer = new ArrayList<>();

    public static void registerRunnableWithInvokeDelay(RunnableWithInvokeDelay rd){
        get().functionsToInvokeBuffer.add(rd);
    }

    public static void changeScene(int newScene){
        sceneIndex = newScene;
        switch (newScene){
            case 0:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown scene!'" + newScene + "'";
                break;
        }
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Dyke game engine";
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public static void removeRunnableWithInvokeDelay(RunnableWithInvokeDelay runnableWithInvokeDelay) {
        get().functionsToInvokeBuffer.remove(runnableWithInvokeDelay);
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
    boolean sceneRefreshFlag = false;

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

        //Setting window size callback
        glfwSetWindowSizeCallback(glfwWindow, (w, xOffset, yOffset) ->{
           Window.setWidth(xOffset);
           Window.setHeight(yOffset);
        });

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

        //Enabling alpha blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();
        glfwMaximizeWindow(glfwWindow);

        //Making current scene
        changeScene(0);
    }

    private void loop() {

        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        glClearColor(1f,1f,1f,1f);
        while (!glfwWindowShouldClose(glfwWindow)){
            KeyListener.endFrame();
            MouseListener.endFrame();
            //Poll events
            glfwPollEvents();


            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

            if (Time.deltaTime >= 0){
                currentScene.update(Time.deltaTime);
            }

            this.imGuiLayer.update(Time.deltaTime, currentScene);

            functionsToInvoke = (ArrayList<RunnableWithInvokeDelay>) functionsToInvokeBuffer.clone();
            for (RunnableWithInvokeDelay rd : functionsToInvoke) {
                rd.update(Time.deltaTime);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            Time.deltaTime = endTime - beginTime;
            beginTime = endTime;

        }

    }

    public static float getWidth(){
        return get().width;
    }

    public static float getHeight(){
        return get().height;
    }

    public static void setWidth(int newWidth){
        get().width = newWidth;
    }

    public static void setHeight(int newHeight){
        get().height = newHeight;
    }
}
