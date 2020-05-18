package ECS.Classes;

import ECS.Classes.Type.ElementalType;

/**
 * 작성날짜 : 오전 2:39 2020-04-08
 * 목    적 :
 *      -- 가디언 팀 색상을 결정하기 위해 만들었음.
 *      -- red, green, blue 각각에 대해 boolean 변수를 할당
 *      -- 팀 내 각 색상속성의 갯수와는 상관없이, 해당 색이 세팅 돼있느냐 마느냐 여부로 T/F를 체크함.
 *          ㄴ 나중에 뭐.. 레벨 값 가지고, 농도로 결정 할 수도 있겠지.
 *
 * 작업이력 :
 *
 *      -- 오전 2:39 2020-04-08
 *          [클래스 생성]
 *          [코드 작성] getMixedRGB() 매서드 작성
 *
 * 메    모 :
 *
 *      -- 오전 3:20 2020-04-08
 *          아 뭔가 묘하게 짬뽕인데... ElementalType이랑 조금 겹치는거 같기도 하고..
 *          다른 점(?)이 있다면, ElementalType은 타입 값을 나타내기 위한 클래스이고,
 *          해당 RGB의 경우, Vector3 클래스처럼 실제 색상 값을 보존하고 있는? 객체로써 쓰이는 거고.
 *
 *
 */
public class RGB {

    /* 멤버 변수 */
    boolean red;
    boolean green;
    boolean blue;

    /* 생성자 */
    public RGB() {

        this.red = false;
        this.green = false;
        this.blue = false;
    }

    /* getter */
    public boolean isRed() {
        return red;
    }

    public boolean isGreen() {
        return green;
    }

    public boolean isBlue() {
        return blue;
    }

    /* setter */
    public void setRed(boolean red) {
        this.red = red;
    }

    public void setGreen(boolean green) {
        this.green = green;
    }

    public void setBlue(boolean blue) {
        this.blue = blue;
    }

    /* 매서드 */

    /**
     * 기능 : 객체에 담긴 RGB 세팅값을 활용하여, 조합 색상을 결정해 리턴한다
     * 사용 :
     *      -- ElementalType 클래스 내에서,
     *          넘겨받은 (플레이어 캐릭터) 색상 목록을 활용하여 팀 색상을 결정하려고 할 때
     *          해당 매서드를 호출함
     *
     * @return
     */
    public int getMixedRGB(){

        int result = ElementalType.NONE;

        /* 색상 판정 */
        boolean isRed = ( isRed() && !isGreen() && !isBlue() ) ;
        boolean isGreen = ( !isRed() && isGreen() && !isBlue() ) ;
        boolean isBlue = ( !isRed() && !isGreen() && isBlue() ) ;
        boolean isYellow = ( isRed() && isGreen() && !isBlue() ) ;
        boolean isPink = ( isRed() && !isGreen() && isBlue() ) ;
        boolean isSkyBlue = ( !isRed() && isGreen() && isBlue() ) ;
        boolean isWhite = ( isRed() && isGreen() && isBlue() ) ;

        /* 결과값 적용*/
        if(isRed){
            result = ElementalType.RED;
            System.out.println("빨");
        }
        else if(isGreen){
            result = ElementalType.GREEN;
            System.out.println("초");
        }else if(isBlue){
            result = ElementalType.BLUE;
            System.out.println("파");
        }else if(isYellow){
            result = ElementalType.YELLOW;
            System.out.println("노");
        }else if(isPink){
            result = ElementalType.PINK;
            System.out.println("분");
        }else if(isSkyBlue){
            result = ElementalType.SKY_BLUE;
            System.out.println("하");
        }else if(isWhite){
            result = ElementalType.WHITE;
            System.out.println("흰");
        } else{
            System.out.println("?? ");
        }

        /* 결과값 리턴 */
        return result;
    }






}
