package populationBoard.dataObjects;
import entities.miniPeople.MiniPerson;

public class Node {

    public MiniPerson mp;
    public String name;
    public Node mother;
    public Node father;

    public Node(MiniPerson mp, String name){
        this.mp = mp;
        this.name = name;
    }
}