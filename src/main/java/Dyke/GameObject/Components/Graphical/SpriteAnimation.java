package Dyke.GameObject.Components.Graphical;

import Dyke.GameObject.Components.Component;
import Dyke.GameObject.GameObject;
import Dyke.util.ArrayUtil;

import java.util.Arrays;

public class SpriteAnimation{
    private Sprite[] KEYFRAMES;
    private float ANIM_TIME;
    private int KEYFRAMES_NUMBER;
    private float KEYFRAME_LENGTH;
    private float timeSinceLastChange;
    private SpriteRenderer spriteRenderer;
    private int frame;
    public String name;
    public SpriteAnimation(String name, float animationTime, boolean mirror ,Sprite... sprites){
        if(mirror){
            Sprite[] flipped = new Sprite[sprites.length];
            for (int i = 0; i < sprites.length; i++){
                flipped[i] = sprites[sprites.length - 1 - i];
            }
            Sprite[] sprites1 = new Sprite[sprites.length * 2];
            KEYFRAMES = ArrayUtil.combineArrays(sprites, flipped).toArray(sprites1);

            ANIM_TIME = animationTime;
            KEYFRAMES_NUMBER = KEYFRAMES.length;
            KEYFRAME_LENGTH = ANIM_TIME / (KEYFRAMES_NUMBER - 1);
            this.name = name;
            timeSinceLastChange = KEYFRAME_LENGTH;
            frame = 0;

        }else {

            KEYFRAMES = sprites;
            ANIM_TIME = animationTime;
            KEYFRAMES_NUMBER = KEYFRAMES.length;
            KEYFRAME_LENGTH = ANIM_TIME / (KEYFRAMES_NUMBER - 1);
            this.name = name;
            timeSinceLastChange = KEYFRAME_LENGTH;
            frame = 0;
        }

    }
    public SpriteAnimation(String name, float animationTime, Sprite... sprites){
        KEYFRAMES = sprites;
        ANIM_TIME = animationTime;
        KEYFRAMES_NUMBER = KEYFRAMES.length;
        KEYFRAME_LENGTH = ANIM_TIME / (KEYFRAMES_NUMBER - 1);
        this.name = name;
        timeSinceLastChange = KEYFRAME_LENGTH;
        frame = 0;
    }

    public void start(SpriteRenderer spriteRenderer) {
        if(spriteRenderer == null){
            assert false: "The game object does not have a Sprite renderer";
        }
        this.spriteRenderer = spriteRenderer;
        this.spriteRenderer.setSprite(KEYFRAMES[frame]);
    }


    public void update(float dt) {
        timeSinceLastChange -= dt;
        if(timeSinceLastChange <= 0f){
            timeSinceLastChange = KEYFRAME_LENGTH;
            frame++;
            if(frame >= KEYFRAMES_NUMBER){
                frame = 0;
            }
        }
        spriteRenderer.setSprite(KEYFRAMES[frame]);
    }


}
