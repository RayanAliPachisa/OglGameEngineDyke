package Dyke.renderer;

import Dyke.GameObject.Components.SpriteRenderer;
import Dyke.Window;
import Dyke.util.AssetPool;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class RenderBatch {
    //For each vertex, 2 floats for pos, 4 floats for colour
    private final int POS_SIZE = 2;
    private final int COLOUR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOUR_OFFSET = (POS_OFFSET + POS_SIZE) * Float.BYTES;
    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize){
        shader = AssetPool.getShader("default.glsl",false);

        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        //4 vertices per quad
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void render(){
        //Re-buffers all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

    public void addSprite(SpriteRenderer spriteRenderer){
        //Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = spriteRenderer;
        this.numSprites ++;

        //Add properties to local vertices array
        loadVertexProperties(index);

        if(numSprites >= this.maxBatchSize){
            this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int index){
        SpriteRenderer spriteRenderer = this.sprites[index];
        int offset = index * 4 * VERTEX_SIZE;
        Vector4f colour = spriteRenderer.colour;

        float xAdd = 1.0f;
        float yAdd = 1.0f;
        //Putting vertices in the order TR,BR,BL,TL
        for(int i = 0; i < 4; i++){
            if(i == 1){
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            }else if(i == 3){
                yAdd = 1.0f;
            }
            //Load position
            vertices[offset] = spriteRenderer.parent.transform.position.x + (xAdd * spriteRenderer.parent.transform.scale.x);
            vertices[offset + 1] = spriteRenderer.parent.transform.position.y + (yAdd * spriteRenderer.parent.transform.scale.y);
            //Load colour
            vertices[offset + 2] = colour.x;
            vertices[offset + 3] = colour.y;
            vertices[offset + 4] = colour.z;
            vertices[offset + 5] = colour.w;

            offset += VERTEX_SIZE;
        }
    }

    public void start(){
        // Generate and bind Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        //Reserving space
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES,
                GL_DYNAMIC_DRAW); // indicating that vertices can change

        //create and upload indices buffer

        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //Enable the buffer attribute pointers for the vertices pos
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        //Enable the buffer attribute pointers for the colours
        glVertexAttribPointer(1,COLOUR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOUR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    private int[] generateIndices(){
        //6 indices per quad (3 indices per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index){
        int offsetArrayindex = 6 * index;
        int offset = 4 * index;
        //3, 2, 0, 0, 2, 1          7, 6, 4, 4, 6, 5
        //Triangle 1
        elements[offsetArrayindex + 0] = offset + 3;
        elements[offsetArrayindex + 1] = offset + 2;
        elements[offsetArrayindex + 2] = offset + 0;
        //Triangle 2
        elements[offsetArrayindex + 3] = offset + 0;
        elements[offsetArrayindex + 4] = offset + 2;
        elements[offsetArrayindex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return hasRoom;
    }

}
