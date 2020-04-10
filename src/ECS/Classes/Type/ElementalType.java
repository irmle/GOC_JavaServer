package ECS.Classes.Type;

import ECS.Classes.RGB;

import java.util.ArrayList;

/**
 * 속성 순서에 변화를 줌.
 * 그린이랑 블루 순서가 뒤바뀌어 있었음. 레-블-그 순으로 맞춤.
 * 나머지 색상도 추가.
 *
 * 작업 이력 :
 *
 *      -- 오후 4:53 2020-04-07
 *          [주석 작성] 색상조합(getMixedElemental) 매서드 추가
 *
 *      -- 오전 3:02 2020-04-08
 *         [코드 작성] getMixedElemental() 매서드 채움
 *
 *
 */
public class ElementalType {

    public static final int NONE = 0;
    public static final int RED = 1;
    public static final int BLUE = 2;
    public static final int GREEN = 3;

    public static final int YELLOW = 4;
    public static final int PINK = 5;
    public static final int SKY_BLUE = 6;

    public static final int WHITE = 7;
    public static final int BLACK = 8;

    /**
     * 수정 & 작성함
     * 업뎃날짜 ; 오전 3:02 2020-04-08
     * 기능 :
     *      -- 주어진 색상 속성 목록을 조합하여, 새로운 속성을 결정한다
     *
     * 사용 :
     *      -- 게임 월드가 생성되기 전, 매칭 인원들끼리 캐릭터 픽을 진행할 때
     *      -- 게임월드 생성 후, 받아온 플레이어 캐릭터 정보에 팀 색상을 지정해줄 때
     *
     */
    public static int getMixedElemental(ArrayList<Integer> elementalList){

        int mixedElemental = ElementalType.NONE;

        /* 존재하는 색상들을 세팅한다 */

        RGB rgb = new RGB();
        int currentElemental;

        System.out.println("사이즈 : " + elementalList.size());
        for (int i=0; i<elementalList.size(); i++){

            currentElemental = elementalList.get(i);
            switch (currentElemental){

                case ElementalType.RED :
                    System.out.println("빨강색 세팅");
                    rgb.setRed(true);
                    break;

                case ElementalType.GREEN :
                    System.out.println("녹색 세팅");
                    rgb.setGreen(true);
                    break;

                case ElementalType.BLUE :
                    System.out.println("파랑색 세팅");
                    rgb.setBlue(true);
                    break;
            }
        }

        /* 세팅된 RGB값을 가지고, 속성 조합 처리를 한다 */
        mixedElemental = rgb.getMixedRGB();

        return mixedElemental;
    }

}
