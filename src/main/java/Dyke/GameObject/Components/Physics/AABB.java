package Dyke.GameObject.Components.Physics;

import Dyke.GameObject.Components.Component;
import Dyke.GameObject.Components.Transform;
import Dyke.GameObject.GameObject;

public class AABB extends Component{
    /**
     * Axis aligned bounding box
     * */

    public Transform transform;
    public AABB(GameObject parent) {
        this.parent = parent;
        this.transform = parent.getComponent(Transform.class);
    }

}
