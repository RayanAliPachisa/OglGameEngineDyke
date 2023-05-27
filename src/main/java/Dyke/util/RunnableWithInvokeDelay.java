package Dyke.util;

import Dyke.Window;

public class RunnableWithInvokeDelay {
    float timeElapsed;
    public float delay;
    Runnable toRun;

    public RunnableWithInvokeDelay(float delay, Runnable toRun) {
        this.delay = delay;
        this.toRun = toRun;
        timeElapsed = 0f;
    }

    public void update(float dt){
        if(timeElapsed >= delay){
            toRun.run();
            Window.removeRunnableWithInvokeDelay(this);
        }else{
            timeElapsed += dt;
        }
    }

    public void reset(){
        timeElapsed = 0f;
    }
}
