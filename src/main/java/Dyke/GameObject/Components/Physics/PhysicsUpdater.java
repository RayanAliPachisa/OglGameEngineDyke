package Dyke.GameObject.Components.Physics;

import Dyke.GameObject.Components.Transform;
import Dyke.GameObject.GameObject;
import Dyke.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Collections;

public class PhysicsUpdater extends Thread{

    GameObject[] gameObjects;


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
        Collections.sort(yList);
        //Converting list to array
        TransformLinkedFloat[] minMaxX = xList.toArray(new TransformLinkedFloat[xList.size()]);
        TransformLinkedFloat[] minMaxY = yList.toArray(new TransformLinkedFloat[yList.size()]);
        ArrayList<Transform> currentCheckY = new ArrayList<>();

        for (TransformLinkedFloat f: minMaxX) {
            if(f.start){
                for (Transform transform : currentCheckY) {
                    //TODO complete this
                }

                currentCheckY.add(f.transform);
            }else{
                currentCheckY.remove(f.transform);
            }

        }
    }
}
