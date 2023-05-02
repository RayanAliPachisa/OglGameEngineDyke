package Dyke.GameObject.Components.Graphical;

import Dyke.GameObject.Components.Component;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager extends Component {
    public String activeName;
    public List<SpriteAnimation> animations;
    public SpriteAnimation activeAnimation;



    public AnimationManager(){
        init(new ArrayList(), -1);
    }

    public AnimationManager(ArrayList animations, int index){
        init(animations, index);
    }

    public AnimationManager(ArrayList animations){
        init(animations, -1);
    }

    public AnimationManager(ArrayList animations, String name){
        for(int i = 0; i < animations.size(); i++){

        }
    }

    public void init(ArrayList animations, int index){
        this.animations = animations;
        activeAnimation = index != -1 ? this.animations.get(index) : null;
        activeName = activeAnimation != null ? activeAnimation.name : "";
    }





    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }
}
