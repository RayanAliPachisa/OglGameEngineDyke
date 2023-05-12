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
    ArrayList<TransformLinkedFloat> xList = new ArrayList<TransformLinkedFloat>();
    ArrayList<TransformLinkedFloat> yList = new ArrayList<TransformLinkedFloat>();
    ArrayList<Transform> transforms = new ArrayList<>();
    TransformLinkedFloat[] minMaxX = xList.toArray(new TransformLinkedFloat[xList.size()]);
    ArrayList<TransformLinkedFloat> currentCheckX = new ArrayList<TransformLinkedFloat>();
    TransformLinkedFloat[] minMaxY = yList.toArray(new TransformLinkedFloat[yList.size()]);
    ArrayList<TransformLinkedFloat> currentCheckY = new ArrayList<TransformLinkedFloat>();

    public void prepare(GameObject[] gameObjects){
        this.gameObjects = gameObjects;
    }

    @Override
    public void run() {
        xList = new ArrayList<TransformLinkedFloat>();
        yList = new ArrayList<TransformLinkedFloat>();
        transforms = new ArrayList<>();

        addingPerGameObject();

        //This sort call is also lagging out the program quite a bit
        //Sorting the lists
        Collections.sort(xList);
        Collections.sort(yList);
        //Converting list to array
        minMaxX = xList.toArray(new TransformLinkedFloat[xList.size()]);
        currentCheckX = new ArrayList<TransformLinkedFloat>();
        minMaxY = yList.toArray(new TransformLinkedFloat[yList.size()]);
        currentCheckY = new ArrayList<TransformLinkedFloat>();
        collisions = new ArrayList<Collision>();

        xColls();
        yColls();
        registerCollisions();
    }

    public void addingPerGameObject(){
        for (GameObject gameObject: gameObjects) {
            //Adding transform
            transforms.add(gameObject.transform);

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
    }

    private void xColls(){
        //Adding to XColls
        for (TransformLinkedFloat f: minMaxX) {
            if(f.start){
                f.transform.xColl = (ArrayList<TransformLinkedFloat>) currentCheckX.clone();
                currentCheckX.add(f);
            }else{
                currentCheckX.remove(f.transform.minX);
            }
        }
    }

    private void yColls(){
        //Adding to YColls
        for(TransformLinkedFloat f: minMaxY){
            if(f.start){
                f.transform.yColl = (ArrayList<TransformLinkedFloat>) currentCheckY.clone();
                currentCheckY.add(f);
            }else{
                currentCheckY.remove(f.transform.minY);
            }
        }
    }

    private void registerCollisions(){
        //Registering collisions
        for(Transform transform: transforms){
            for (TransformLinkedFloat tlf: transform.xColl) {
                //The contains method is lagging up the entire program the most
                if(transform.yColl.contains(tlf.transform.minY)){
                    collisions.add(new Collision(transform, tlf.transform));
                }
            }
        }
    }


}