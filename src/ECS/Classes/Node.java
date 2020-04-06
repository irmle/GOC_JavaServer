package ECS.Classes;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 01 14
 * 업뎃날짜 :
 * 목    적 : A* 알고리즘에 사용할 노드를 구현
 */
public class Node {

    /* 멤버 변수 */
    private MapInfo tile;   // 노드가 가리키는 타일
    private Node parentNode;    // 부모 노드, 초기값 NULL

    public float f;
    public float g;
    public float h;

    public boolean isClosed;

    /* 생성자 */
    public Node(MapInfo tile) {

        this.tile = tile;
        initNode();

    }

    /* 매서드 */

    /**
     * 노드의 g, h, f 값을 세팅하는 매서드
     * @param g
     * @param h
     * @param f
     */
    public void setNodeValue(float g, float h, float f){

        this.g = g;
        this.h = h;
        this.f = f;

    }

    /**
     * 노드 초기화 함수.
     * 길 찾기 매서드를 새로 호출할 때 마다, 알고리즘 수행 전에 모든 노드를 초기화해줘야 하는데, 그 때 불린다.
     */
    public void initNode(){

        parentNode = null;
        isClosed = false;

        f = 0f;
        g = 0f;
        h = 0f;

    }

    /**
     * 부모 노드 세팅하는 매서드
     * @param parentN
     */
    public void setParentNode(Node parentN){

        this.parentNode = parentN;
    }

    /**
     * 부모 노드 겟
     * @return
     */
    public Node getParentNode() {
        return parentNode;
    }

    /**
     * 노드가 가리키는 타일 정보를 리턴하는 함수
     * @return
     */
    public MapInfo getTile(){
        return tile;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
