package populationBoard.boardBiology;

import java.util.HashMap;

import entities.miniPeople.MiniPerson;
import entities.dataObjects.Gender;
import populationBoard.dataObjects.Node;

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

    public Node getFather(MiniPerson mp){
        Node currentMp = ancestry.get(mp);
        return currentMp.father;
    }

    public Node getMother(MiniPerson mp){
        Node currentMp = ancestry.get(mp);
        return currentMp.mother;
    }

    public String getFatherName(MiniPerson mp){
        return getFather(mp).name;
    }

    public String getMotherName(MiniPerson mp){
        return getMother(mp).name;
    }

    public String[] getParentsNames(MiniPerson mp){
        String [] ret = new String[2];
        ret[0] = getFather(mp).name;
        ret[1] = getMother(mp).name;
        return ret;
    }

    public Node[] getParents(MiniPerson mp){
        Node currentMp = ancestry.get(mp);
        Node[] ret = new Node[2];
        ret[0] = currentMp.father;
        ret[1] = currentMp.mother;
        return ret;
    }
}

