package Dyke.GameObject.Components.Graphical;

import Dyke.GameObject.Components.Component;
import Dyke.GameObject.GameObject;

public class SpriteAnimation extends Component {
    private Sprite[] KEYFRAMES;
    private float ANIM_TIME;
    private int KEYFRAMES_NUMBER;
    private float KEYFRAME_LENGTH;
    private float timeSinceLastChange;
    private SpriteRenderer spriteRenderer;
    private int frame;
    public SpriteAnimation(float animationTime, Sprite... sprites){
        KEYFRAMES = sprites;
        ANIM_TIME = animationTime;
        KEYFRAMES_NUMBER = KEYFRAMES.length;
        KEYFRAME_LENGTH = ANIM_TIME / (KEYFRAMES_NUMBER - 1);
        timeSinceLastChange = KEYFRAME_LENGTH;
        frame = 0;
    }

    @Override
    public void start() {
        if(parent.getComponent(SpriteRenderer.class) == null){
            assert false: "The game object '" + parent.name + "' does not have a Sprite renderer";
        }
        spriteRenderer = parent.getComponent(SpriteRenderer.class);
        spriteRenderer.setSprite(KEYFRAMES[frame]);
    }

    @Override
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
