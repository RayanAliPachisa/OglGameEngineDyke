package Dyke.GameObject.Components.Graphical;

import Dyke.GameObject.Components.Component;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager extends Component {
    public String activeName;
    public List<SpriteAnimation> animations;
    public SpriteAnimation activeAnimation;
    boolean activated = false;
    public Sprite defaultSprite;
    private SpriteRenderer spriteRenderer;

    public AnimationManager(Sprite defaultSprite) {
        init(defaultSprite, new ArrayList(), -1);
    }

    public AnimationManager(Sprite defaultSprite, ArrayList animations, int index) {
        init(defaultSprite, animations, index);
    }

    public AnimationManager(Sprite defaultSprite, ArrayList animations) {
        init(defaultSprite, animations, -1);
    }

    public AnimationManager(Sprite defaultSprite, ArrayList animations, String name) {
        boolean flag = false;
        for (int i = 0; i < animations.size(); i++) {
            if (((SpriteAnimation) animations.get(i)).name == name) {
                flag = true;
                activeName = name;
                init(defaultSprite, animations, i);
                break;
            }
        }
        if (!flag) {
            assert false : "Animation with the name'" + name + "' does not exist";
        }
    }

    private void init(Sprite defaultSprite, ArrayList animations, int index) {
        this.defaultSprite = defaultSprite;
        this.animations = animations;
        activated = true;
        activeAnimation = index != -1 ? this.animations.get(index) : null;
        activeName = activeAnimation != null ? activeAnimation.name : "";
    }

    public void startAnimation(int index) {
        activeAnimation = index != -1 ? animations.get(index) : null;
        activeName = activeAnimation != null ? activeAnimation.name : "";
        activated = true;
    }

    public void startAnimation(String name) {
        boolean flag = false;
        for (int i = 0; i < animations.size(); i++) {
            if (((SpriteAnimation) animations.get(i)).name == name) {
                flag = true;
                activeName = name;
                startAnimation(i);
                break;
            }
        }
        if (!flag) {
            assert false : "Animation with the name'" + name + "' does not exist";
        }
    }

    public void stopAnimation() {
        activeAnimation = null;
        activeName = "";
        activated = true;
    }


    @Override
    public void start() {
        spriteRenderer = parent.getComponent(SpriteRenderer.class);
    }

    @Override
    public void update(float dt) {
        if(activated){
            if(activeAnimation == null){
                spriteRenderer.setSprite(defaultSprite);
            }else{
                activeAnimation.start(spriteRenderer);
            }
            activated = false;
        }else{
            if(activeAnimation != null){
                activeAnimation.update(dt);
            }
        }
    }

    public List<SpriteAnimation> getAnimations() {
        return animations;
    }

    public void setAnimations(List<SpriteAnimation> animations) {
        this.animations = animations;
    }

    public SpriteAnimation getActiveAnimation() {
        return activeAnimation;
    }

    public void setActiveAnimation(SpriteAnimation activeAnimation) {
        this.activeAnimation = activeAnimation;
    }

    public Sprite getDefaultSprite() {
        return defaultSprite;
    }

    public void setDefaultSprite(Sprite defaultSprite) {
        this.defaultSprite = defaultSprite;
    }

    public void addAnimation(SpriteAnimation animation){
        animations.add(animation);
    }


}
