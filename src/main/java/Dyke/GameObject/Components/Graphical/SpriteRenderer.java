package Dyke.GameObject.Components.Graphical;

import Dyke.GameObject.Components.Component;
import Dyke.GameObject.Components.Transform;
import Dyke.renderer.RenderBatch;
import Dyke.renderer.Texture;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    Vector4f colour;
    Sprite sprite;
    private RenderBatch renderBatch;
    Vector2f[] texCoords = new Vector2f[]{};
    Transform lastTransform;
    //For dirty flagging
    //TODO when moving sprites from one batch to another, need to change the texID
    boolean dirty = true;

    public SpriteRenderer(Vector4f colour) {
        super();
        this.colour = colour;
        //Creating null sprite
        this.sprite = new Sprite();
        dirty = true;
    }

    public SpriteRenderer(Sprite sprite) {
        super();
        this.sprite = sprite;
        this.colour = new Vector4f(1,1,1,1);
        this.texCoords = sprite.getTexCoords();
        dirty = true;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public Vector4f getColour() {
        return colour;
    }

    public Sprite getSprite() {
        return sprite;
    }

    //Dirty flagging if data is changed
    public void setColour(Vector4f colour) {
        if(!this.colour.equals(colour)){
            dirty = true;
        }
        this.colour = colour;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.texCoords = sprite.getTexCoords();
        dirty = true;
    }


    @Override
    public void start() {
        this.lastTransform = parent.transform.copy();
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setClean() {
        this.dirty = false;
    }
    public void setDirty(){this.dirty = true;}

    @Override
    public void update(float dt) {
        if(this.lastTransform == null){
            this.lastTransform = parent.transform.copy();
            return;
        }
        if(!this.lastTransform.equals(this.parent.transform)){
            this.parent.transform.copyTo(this.lastTransform);
            //Checking if transform has changed and changing the dirty flag
            dirty = true;
        }
    }

    @Override
    public void imgui() {
        ImGui.begin("Hi!");
        float[] imColour = {colour.x, colour.y, colour.z, colour.w};
        if(ImGui.colorPicker4("Colour picker", imColour)){
            this.colour.set(imColour[0], imColour[1], imColour[2], imColour[3]);
            dirty = true;
        }

        ImGui.end();
    }

    public RenderBatch getRenderBatch() {
        return renderBatch;
    }

    public void setRenderBatch(RenderBatch renderBatch) {
        this.renderBatch = renderBatch;
    }
}
