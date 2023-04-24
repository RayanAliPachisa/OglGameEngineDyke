package Dyke.Game.Scene;

import Dyke.renderer.Camera;
import Dyke.renderer.Shader;
import Dyke.renderer.Texture;
import Dyke.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class LevelScene extends Scene{
    float timeSinceStart = 0f;
    float[] smootherStack = new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,0f};
    private String vertexShaderSrc = "#version 330 core\n" +
            "    layout(location = 0) in vec3 aPos;\n" +
            "    layout(location = 1) in vec4 aColor;\n" +
            "    \n" +
            "    out vec4 fColor;\n" +
            "    \n" +
            "    void main(){\n" +
            "        fColor = aColor;\n" +
            "        gl_Position = vec4(aPos, 1.0);\n" +
            "    }\n" +
            "    ";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "    \n" +
            "    in vec4 fColor;\n" +
            "    \n" +
            "    out vec4 color;\n" +
            "    \n" +
            "    void main(){\n" +
            "        color = fColor;\n" +
            "    }";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {                 //UV coords
             50f, -50f, 0.0f,    1f,0f,0f,1.0f,     1, 1,//Bottom-right
            -50f, 50f, 0.0f,     0f,1f,0f,1.0f,     0, 0,//Top-left
             50f,50f,0.0f,       0f,0f,1f,1.0f,     1, 0,//Top-right
            -50f,-50f,0.0f,      1f,1f,0f,1.0f,     0, 1//Bot-left
    };
    //IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            2,1,0, //Top right triangle
            0,1,3
    };

    private int vaoID,vboID,eboID;
    private Shader defualtShader = new Shader("Assets/Shaders/default.glsl");
    private Texture testTexture;

    public LevelScene(){
        super();
        System.out.println("Hello Scene!");
    }


    @Override
    public void init(){
        this.camera = new Camera(new Vector2f(-500,-500));
        defualtShader.compile();

        glActiveTexture(GL_TEXTURE0);
        this.testTexture = new Texture("Assets/Textures/img.png");
        defualtShader.uploadTexture("TEX_SAMPLER", 0);
        testTexture.bind();


        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        //Add the vertex attribute buffers
        int positionsSize = 3;
        int colourSize = 4;
        int uvSize = 2;
        //A float is 4 bytes
        int floatSizeBytes = Float.BYTES;
        //How big a vertex is
        int vertexSizeBytes = (positionsSize + colourSize + uvSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colourSize,GL_FLOAT,false,vertexSizeBytes,positionsSize*floatSizeBytes);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2,uvSize,GL_FLOAT,false,vertexSizeBytes, (positionsSize + colourSize)*floatSizeBytes);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {

        for(int i = 0; i < smootherStack.length; i++){
            if(i != smootherStack.length - 1){
                smootherStack[i] = smootherStack[i+1];
            }else{
                smootherStack[i] = dt;
            }
        }
        float sdt = 0f;
        for(float i:smootherStack){sdt += i;}
        sdt /= smootherStack.length;


        defualtShader.use();
        defualtShader.uploadMat4f("uProjection",camera.getProjectionMatrix());
        defualtShader.uploadMat4f("uView",camera.getViewMatrix());
        defualtShader.uploadFloat("uTime", Time.getTime());

        //Bind the VAO that we're using
        glBindVertexArray(vaoID);

        //Enable the vertex attribute pointer
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defualtShader.detach();

    }

}
