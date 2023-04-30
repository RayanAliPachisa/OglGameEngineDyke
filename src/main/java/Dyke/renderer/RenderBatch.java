package Dyke.renderer;

import Dyke.GameObject.Components.SpriteRenderer;
import Dyke.Window;
import Dyke.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class RenderBatch implements Comparable<RenderBatch>{
    //For each vertex, 2 floats for pos, 4 floats for colour, 2 floats for tex coords and a float for tex id
    private final int POS_SIZE = 2;
    private final int COLOUR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOUR_OFFSET = (POS_OFFSET + POS_SIZE) * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOUR_OFFSET + COLOUR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = POS_SIZE + COLOUR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
    private final int MAX_TEXTURES = 8;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0,1,2,3,4,5,6,7};

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;
    private List<Texture> textures;
    private int zIndex;

    public RenderBatch(int maxBatchSize){
        new RenderBatch(maxBatchSize, 0);
    }

    public RenderBatch(int maxBatchSize, int zIndex){
        this.zIndex = zIndex;
        shader = AssetPool.getShader("default.glsl",false);

        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        //4 vertices per quad
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }

    public void render(){
        boolean rebufferData = false;
        for (int i = 0; i < numSprites; i++){
            SpriteRenderer spriteRenderer = sprites[i];
            if (spriteRenderer.isDirty()){
                rebufferData = true;
                //refreshing properties for the dirty sprite
                loadVertexProperties(i);
                //Setting the dirty flag off
                spriteRenderer.setClean();

            }
        }

        if (rebufferData){
            //Re-buffers all data every frame
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());
        for (int i = 0; i < textures.size(); i++){
            //Activate texture in appropriate slot
            glActiveTexture(GL_TEXTURE0 + i + 1);
            //Binding the relevant texture
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        //Enabling the stuff to send for per vertex
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        //Unbinding everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        for (int i = 0; i < textures.size(); i++){
            //Activate texture in appropriate slot
            glActiveTexture(GL_TEXTURE0 + i + 1);
            //Binding the relevant texture
            textures.get(i).unBind();
        }

        glBindVertexArray(0);
        shader.detach();
    }

    public boolean checkAddSprites(SpriteRenderer spriteRenderer){
        //Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = spriteRenderer;
        this.numSprites ++;

        if(spriteRenderer.getTexture() != null){
            if(!textures.contains(spriteRenderer.getTexture())){
                if(textures.size() <= MAX_TEXTURES){
                    //Textures does not contain but have space to add another texture
                    return true;
                }else{
                    //Textures does not contain and no space to add another texture
                    return false;
                }
            }
            //textures contain this guy's texture so can add
            return true;
        }else {
            //This guys has no texture so can add
            return true;
        }
    }

    public void addSprite(SpriteRenderer spriteRenderer){
        //Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = spriteRenderer;
        this.numSprites ++;

        if(spriteRenderer.getTexture() != null){
            if(!textures.contains(spriteRenderer.getTexture())){
                textures.add(spriteRenderer.getTexture());
            }
        }

        //Add properties to local vertices array
        loadVertexProperties(index);

        if(numSprites >= this.maxBatchSize){
            this.hasRoom = false;
        }
    }

    //Loads each vertex property for a given sprite
    private void loadVertexProperties(int index){
        SpriteRenderer spriteRenderer = this.sprites[index];
        int offset = index * 4 * VERTEX_SIZE;
        Vector4f colour = spriteRenderer.getColour();
        Vector2f[] texCoords = new Vector2f[]{};
        int texId = 0;

        //Checking if there is no texture
        if(spriteRenderer.getTexture().getTexID() == 0){
            texId = 0;
            texCoords = spriteRenderer.getSprite().getTexCoords();
        }else{
            texCoords = spriteRenderer.getTexCoords();
            //Finding the texture id
            if (spriteRenderer.getTexture() != null){
                for(int i = 0; i < textures.size(); i++){
                    if(textures.get(i) == spriteRenderer.getTexture()){
                        //Making 0 a special reserved slot with no texture
                        texId = i + 1;
                        break;
                    }
                }
            }
        }


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

            //Load tex coords
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;
            //Load tex id
            vertices[offset + 8] = texId;
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
        //Enable the buffer attribute pointers for the colours
        glVertexAttribPointer(2,TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);
        //Enable the buffer attribute pointers for the colours
        glVertexAttribPointer(3,TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
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

    public int getZIndex() {
        return zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.getZIndex(), o.getZIndex());
    }
}
