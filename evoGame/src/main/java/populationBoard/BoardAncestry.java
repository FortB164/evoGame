package populationBoard;

import miniPeople.MiniPerson;
import miniPeople.dataObjects.Gender;
import miniPeople.dataObjects.Node;

import java.util.HashMap;

public class BoardAncestry {

    HashMap<MiniPerson, Node> ancestry = new HashMap<>();
    BoardNames names;

    public BoardAncestry(BoardNames names){
        this.names = names;
    }

    public void handleAncestry(MiniPerson child, MiniPerson parent1, MiniPerson parent2) {
        MiniPerson father = (parent1.getGender() == Gender.MALE) ? parent1 : parent2;
        MiniPerson mother = (parent1.getGender() == Gender.MALE) ? parent2 : parent1;

        Node childNode = new Node(child, names.getNameOf(child));
        childNode.father = ancestry.get(father);
        childNode.mother = ancestry.get(mother);

        ancestry.put(child, childNode);
    }

}
