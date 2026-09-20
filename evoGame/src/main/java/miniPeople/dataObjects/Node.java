package miniPeople.dataObjects;
import miniPeople.MiniPerson;

public class Node {

    public MiniPerson child;
    public String childName;
    public Node mother;
    public Node father;

    public Node(MiniPerson child, String childName){
        this.child = child;
        this.childName = childName;
    }

}
