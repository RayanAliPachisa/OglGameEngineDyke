package Dyke.renderer;

import Dyke.GameObject.Components.Graphical.SpriteRenderer;
import Dyke.GameObject.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer(){
        this.batches = new ArrayList<>();
    }

    public void add(GameObject gameObject){
        SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        if (spriteRenderer != null){
            add(spriteRenderer);
        }
    }

    public void add(SpriteRenderer spriteRenderer){
        boolean added = false;
        for (RenderBatch batch: batches){
            if (batch.hasRoom() && batch.checkAddSprites(spriteRenderer) && batch.getZIndex() == spriteRenderer.parent.getZIndex()){
                batch.addSprite(spriteRenderer);
                added = true;
                spriteRenderer.setRenderBatch(batch);
                break;
            }
        }
        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, spriteRenderer.parent.getZIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(spriteRenderer);
            spriteRenderer.setRenderBatch(newBatch);
            //Sorts every time a new batch is added
            Collections.sort(batches);
        }
    }

    public void changeShaderAll(String name){
        for (RenderBatch rb: batches) {
            rb.setShader(name);
        }
    }

    public void render(){
        for (RenderBatch batch: batches){
            batch.render();
        }
    }

}
