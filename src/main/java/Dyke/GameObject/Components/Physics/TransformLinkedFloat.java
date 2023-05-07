package Dyke.GameObject.Components.Physics;

import Dyke.GameObject.Components.Transform;

public class TransformLinkedFloat implements Comparable<TransformLinkedFloat>{
    public Transform transform;
    boolean start;
    public float f;

    public TransformLinkedFloat(Transform transform, float f, boolean start) {
        this.transform = transform;
        this.f = f;
    }

    @Override
    public int compareTo(TransformLinkedFloat o) {
        if(f > o.f){
            return  1;
        }else{
            return -1;
        }
    }
}
