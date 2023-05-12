package Dyke.GameObject.Components.Physics;

import Dyke.GameObject.Components.Graphical.SpriteRenderer;
import Dyke.GameObject.Components.Transform;
import Dyke.GameObject.GameObject;
import Dyke.util.ArrayUtil;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Collections;

import static Dyke.util.ArrayUtil.lowerBoundFloatLinkedList;

public class PhysicsUpdater extends Thread{

    GameObject[] gameObjects;
    ArrayList<Collision> collisions;

    public void prepare(GameObject[] gameObjects){
        this.gameObjects = gameObjects;
    }

    @Override
    public void run() {
        ArrayList<TransformLinkedFloat> xList = new ArrayList<TransformLinkedFloat>();
        ArrayList<TransformLinkedFloat> yList = new ArrayList<TransformLinkedFloat>();
        for (GameObject gameObject: gameObjects) {
            //Adding min X
            TransformLinkedFloat minX = new TransformLinkedFloat(gameObject.transform, gameObject.transform.position.x,true);
            xList.add(minX);
            gameObject.transform.minX = minX;

            //Adding max X
            TransformLinkedFloat maxX = new TransformLinkedFloat(gameObject.transform, gameObject.transform.position.x + gameObject.transform.scale.x,false);
            xList.add(maxX);
            gameObject.transform.maxX = maxX;

            //Adding min Y
            TransformLinkedFloat minY = new TransformLinkedFloat(gameObject.transform, gameObject.transform.position.y,true);
            yList.add(minY);
            gameObject.transform.minY = minY;

            //Adding max Y
            TransformLinkedFloat maxY = new TransformLinkedFloat(gameObject.transform, gameObject.transform.position.y + gameObject.transform.scale.y,false);
            yList.add(maxY);
            gameObject.transform.maxY = maxY;

        }

        //Sorting the list
        Collections.sort(xList);
        //Converting list to array
        TransformLinkedFloat[] minMaxX = xList.toArray(new TransformLinkedFloat[xList.size()]);
        ArrayList<TransformLinkedFloat> currentCheckY = new ArrayList<TransformLinkedFloat>();
        collisions = new ArrayList<Collision>();
        //TODO implement Yu Yao's technique
        System.out.println("Length of minmaxX:" + minMaxX.length);
        for (TransformLinkedFloat f: minMaxX) {
            if(f.start){
                TransformLinkedFloat[] toCheck = currentCheckY.toArray(new TransformLinkedFloat[]{});
                for (TransformLinkedFloat transform : toCheck) {
                    if((f.f > transform.transform.minY.f && f.f < transform.transform.maxY.f) || (f.transform.maxY.f > transform.transform.minY.f && f.transform.maxY.f < transform.transform.maxY.f)){
                        collisions.add(new Collision(f.transform, transform.transform));
                    }
                }


                currentCheckY.add(f);
                Collections.sort(currentCheckY);
            }else{
                currentCheckY.remove(f.transform.minX);
            }
        }
    }
}
