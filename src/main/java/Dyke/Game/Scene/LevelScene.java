package Dyke.Game.Scene;

import Dyke.GameObject.Components.Graphical.*;
import Dyke.GameObject.GameObject;
import Dyke.Input.KeyListener;
import Dyke.Window;
import Dyke.renderer.Camera;
import Dyke.renderer.Shader;
import Dyke.renderer.Texture;
import Dyke.util.AssetPool;
import Dyke.util.FileWindowHandler;
import Dyke.util.RunnableWithInvokeDelay;
import imgui.ImFloat;
import imgui.ImGui;
import imgui.ImInt;
import imgui.ImString;
import org.joml.Vector2f;
import org.joml.Vector4f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;


public class  LevelScene extends Scene{
    float timeSinceStart = 0f;
    Dictionary<String, GameObject> nameGameObjectDictionary = new Hashtable<>();
    float rotation;
    GameObject currentImage;
    String error;
    ImInt chosenShader = new ImInt(0);
    private ImString shaderProgram = new ImString("default");
    private Shader defualtShader = new Shader("Assets/Shaders/default.glsl");
    private Texture testTexture;
    private GameObject obj1;
    private GameObject nameGO;
    private Spritesheet sprites;
    float rollElapsed = 0f;
    float rollDuration = 5f;
    float flickerMin = 0.8f;
    float flickerMax = 1.0f;
    boolean rollFlag = false;
    boolean settingsOpen = false;
    int baseZ = 0;
    private ImString name = new ImString();
    ArrayList names;

    RunnableWithInvokeDelay changeImage = new RunnableWithInvokeDelay(0.1f, this::changeImage);

    public LevelScene(){
        super();
        System.out.println("Hello Scene!");
    }


    @Override
    public void init(){
        //Calls to base class
        super.init();
        loadResources();
        this.camera = new Camera(new Vector2f());
        GameObject gb = new GameObject("Hi!", new Vector2f(500,250), new Vector2f(256,256),-1);
        gb.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("obamaPrism.png",false))));
        addGameObjectToScene(gb);
    }

    private void loadResources(){
        AssetPool.getShader("default.glsl",false);

        AssetPool.addSpritesheet("obamaPrism.png",
                new Spritesheet(
                        AssetPool.getTexture("obamaPrism.png",false),
                        new SpritesheetLine(140, 84, 2, 20),
                        new SpritesheetLine(140, 84,2, 20)
                ),

                false

        );

        AssetPool.addSpritesheet("KlebV2-Sheet.png",
                new Spritesheet(
                        AssetPool.getTexture("KlebV2-Sheet.png",false),
                        85,85,30,1,14
                ),
                false
        );


    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if(rollFlag){
            rollElapsed += dt;
        }
    }

    @Override
    public void imgui() {
        ImString prevName = new ImString(name.get());

        ImGui.begin("Test Window");
        ImGui.inputTextMultiline("", name);
        if(ImGui.button("Randomly choose a person!") && !rollFlag){

            String[] names = name.get().split("\n");
            for(int i = 0; i < names.length; i++){
                //Preparing to choose a person
                Enumeration en = nameGameObjectDictionary.keys();

                //Removing people who don't exist anymore
                while(en.hasMoreElements()) {
                    String key = (String) en.nextElement();
                    if(!name.get().contains(key)){
                        removeGameObjectFromScene(nameGameObjectDictionary.get(key));
                        nameGameObjectDictionary.remove(key);
                    }
                }

                if(nameGameObjectDictionary.get(names[i]) == null){
                    File tempDir = new File("Assets/People/" + names[i] + ".png");
                    if(tempDir.exists()){
                        Texture texture = AssetPool.getTexture("Assets/People/" + names[i] + ".png",true);
                        GameObject gb = new GameObject(names[i], new Vector2f(500,250), new Vector2f(256,256), 0);
                        gb.addComponent(new SpriteRenderer(new Sprite(texture)));
                        nameGameObjectDictionary.put(names[i], gb);
                        addGameObjectToScene(gb);
                    }else{
                        //TODO Indicate that you need to sub in with a text-name

                    }

                }
            }

            this.changeImage.delay = (flickerMin + flickerMax)/2;
            Window.registerRunnableWithInvokeDelay(this.changeImage);

        }

        if(ImGui.button("Add people's images!") &&!rollFlag){
            File[] files = FileWindowHandler.openFileExplorerWindow();
            for (File file:files) {
                ImGui.text("Loading " + file.getName());
                String fileName= file.getName();
                int dotIndex = fileName.lastIndexOf(".");
                String name = fileName.substring(0, dotIndex);
                String extension = fileName.substring(dotIndex);
                InputStream is;
                OutputStream os;
                System.out.println(extension);
                if(extension == ".png"){
                    try{
                        is = new FileInputStream(file);
                        File of = new File("Assets/People/" + name + ".png");
                        if(!of.exists()){
                            System.out.println("Does not exist!");
                            of.createNewFile();
                        }
                        os = new FileOutputStream(of);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }

                    }catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (".jpg".equals(extension)) {
                    BufferedImage bufferedImage = null;
                    try {
                        bufferedImage = ImageIO.read(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // write the bufferedImage back to outputFile
                    try {
                        File of = new File("Assets/People/" + name + ".png");
                        System.out.println("hello team");
                        if(!of.exists()){
                            of.createNewFile();
                            System.out.println("Does not exist!");
                        }
                        ImageIO.write(bufferedImage, "png", of);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        if(ImGui.button("Import Class names") && !rollFlag){
            File[] files = FileWindowHandler.openFileExplorerWindow();
            name.set("");
            for (File file:files) {
                Scanner reader;
                try {
                    reader = new Scanner(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String fromFile = "";

                while(reader.hasNext()){
                    fromFile += reader.nextLine() + "\n";
                }

                name.set(name.get() + fromFile);
            }
        }

        File shaderPath = new File("Assets/Shaders");
        String[] shaders = shaderPath.list(); // eventually for the shader selection
        int prevShader = chosenShader.get();

        ImGui.listBox("Shader choice", chosenShader, shaders, shaders.length,4);

        if(chosenShader.get() != prevShader){
            //TODO need to chgange the shader
            ///TIDO: make obama beanhdead
            renderer.changeShaderAll(shaders[chosenShader.get()]);
        }

        if(ImGui.button("Open Settings")){
            settingsOpen = true;
        }

        ImGui.end();

        ImGui.begin("Chosen name");
        if(currentImage != null){
            ImGui.text("Chosen name:" + currentImage.name);
        }else{
            ImGui.text("Chosen name:" + "Obama");
        }

        ImGui.end();

        if(settingsOpen){
            ImFloat rollDurationIm = new ImFloat(this.rollDuration);
            ImFloat minRangeFlicker = new ImFloat(flickerMin);
            ImFloat maxRangeFlicker = new ImFloat(flickerMax);

            ImGui.begin("Settings");
            ImGui.inputFloat("Roll duration", rollDurationIm);
            this.rollDuration = rollDurationIm.get();

            ImGui.inputFloat("Flicker Min", minRangeFlicker);
            this.flickerMin = minRangeFlicker.get();

            ImGui.inputFloat("Flicker Max", maxRangeFlicker);
            this.flickerMax = maxRangeFlicker.get();

            this.rollDuration = rollDurationIm.get();

            if(ImGui.button("Close")){
                settingsOpen = false;
            }
            ImGui.end();
        }
    }

    public void changeImage(){
        rollFlag = true;
        if(currentImage != null){
            currentImage.changeZIndex(0);
        }
        List gb = Collections.list(nameGameObjectDictionary.elements());
        Random random = new Random();
        currentImage = (GameObject) gb.get(random.nextInt(0,gb.size()));
        currentImage.changeZIndex(5);
        this.changeImage.reset();
        this.changeImage.delay = random.nextFloat(flickerMin,flickerMax);
        System.out.println(this.changeImage.delay);
        //Stopping once roll duration is elapsed
        if(rollElapsed > rollDuration){
            System.out.println("Done");
            rollElapsed = 0f;
            rollFlag = false;
            return;
        }

        Window.registerRunnableWithInvokeDelay(this.changeImage);
        //Register change image again
    }
}
