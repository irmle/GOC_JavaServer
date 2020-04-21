package ECS.Classes;

import ECS.Components.FlyingObjectComponent;

public class Vector3 implements Cloneable {

    private float x = 0f;
    private float y = 0f;
    private float z = 0f;

    public static final Vector3 zero = new Vector3(0f, 0f, 0f);
    public static final Vector3 up = new Vector3(0f, 0f, 1f);

    public Vector3()
    {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public float x()
    {
        return this.x;
    }

    public float y()
    {
        return this.y;
    }

    public float z()
    {
        return this.z;
    }

    public void x(float x)
    {
        this.x = x;
    }

    public void y(float y)
    {
        this.y = y;
    }

    public void z(float z)
    {
        this.z = z;
    }

    public Vector3 setSpeed(float speed)
    {
        this.x *= speed;
        this.y *= speed;
        this.z *= speed;
        return this;
    }

    public Vector3 setSpeed(int speed)
    {
        this.x *= speed;
        this.y *= speed;
        this.z *= speed;
        return this;
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3 target)
    {
        this.x = target.x;
        this.y = target.y;
        this.z = target.z;
    }


    // 외적 구하기
    // 음.. y, z 기준을 클라로 뒤야할지, 그냥 해야할지.. 일단은 그냥 함. 결과는 부호 바뀜 정도인거 같은데
    public static Vector3 getCrossProduct(Vector3 a, Vector3 b){

        Vector3 cross = new Vector3();

        cross.x = + ((a.y * b.z) - (a.z * b.y));
        cross.y = - ((a.x * b.z) - (a.z * b.x));
        cross.z = + ((a.x * b.y) - (a.y * b.x));

        return cross;
    }

    // 직선(백터)과 점 사이의 거리 구하기

    /**
     *
     * @param vector 직선 방향 벡터.
     * @param startPos
     * @param targetPos
     * @return
     */
    public static float getDistanceBetweenVectorAndDot(Vector3 vector, Vector3 startPos, Vector3 targetPos){

        float distance;

        // 시작(시전자) 위치에서 타겟 위치로의 백터
        Vector3 toTargetVec = Vector3.getTargetDirection(startPos, targetPos);

        //System.out.println("시작 -> 타겟 백터 : " + toTargetVec.x() + ", " + toTargetVec.y() + ", " + toTargetVec.z());

        // 위 벡터와 직선 방향 벡터의 외적
        Vector3 cross = Vector3.getCrossProduct(toTargetVec, vector);

        //System.out.println("시작 -> 타겟 백터와 직선백터의 외적 : " + cross.x() + ", " + cross.y() + ", " + cross.z());


        distance = ( Vector3.distance(new Vector3(), cross) / Vector3.distance(new Vector3(0, 0, 0), vector) );

        return distance;
    }

    //내적 구하기
    public static float dot(Vector3 source, Vector3 target)
    {
        float dot = source.x * target.x + source.y * target.y + source.z * target.z;

        return dot;
    }

    //두 벡터 사이의 사이각 구하기 0~180도
    public static float getAngle(Vector3 source, Vector3 target)
    {
        float dot = dot(source, target);
        float length = source.length() * target.length();
        float calc = dot / length;

/*

        System.out.println("내적 구하기 값 : "  + dot);
        System.out.println("길이 구하기 값 : "  + length);
        System.out.println("내적 / 길이 값 : "  + calc);
*/

        //-1f ~ 1f의 범위를 넘어가면 안된다.
        if(calc > 1f)
            calc = 1f;
        else if(calc < -1f)
            calc = -1f;

        return (float) Math.toDegrees( Math.acos(calc) );
    }


    //제곱근 연산을 진행함.
    public static float distance(Vector3 source, Vector3 target)
    {
        float dx = target.x - source.x;
        float dy = target.y - source.y;
        float dz = target.z - source.z;

        /*System.out.println("target : " + target.x + ", " + target.x());
        System.out.println("source : " + source.x + ", " + source.x());

        System.out.println("dx : " + dx);
        System.out.println("dy : " + dy);
        System.out.println("dz : " + dz);
*/
        float result = (float) Math.sqrt(dx*dx + dy*dy + dz*dz);

  //      System.out.println("길이 : " + result);

        if(result<0) //음수일 경우, 양수로 만들어 줄 것.
            result *= -1f;

        return result;
    }

    //제곱근 연산을 제외함.
    public static float sqrMagnitudeDistance(Vector3 source, Vector3 target)
    {
        float dx = target.x - source.x;
        float dy = target.y - source.y;
        float dz = target.z - source.z;

        float result = (dx*dx + dy*dy + dz*dz);

        if(result<0) //음수일 경우, 양수로 만들어 줄 것.
            result *= -1f;

        return result;
    }

    public float length()
    {
        float x = this.x;
        float y = this.y;
        float z = this.z;
        float length = (float) Math.sqrt(x*x + y*y + z*z);

        return length;
    }

    public static Vector3 normalizeVector(Vector3 source, Vector3 target)
    {
        float length = Vector3.distance(source, target);
        float dx = target.x - source.x;
        float dy = target.y - source.y;
        float dz = target.z - source.z;

        dx = dx/length; // dx /= length;
        dy = dy/length; // dy /= length;
        dz = dz/length; // dz /= length;

        //source > target방향을 가리키는, 크기가 1인 벡터를 반환.
        Vector3 normalizeVec = new Vector3(dx, dy, dz);

        return  normalizeVec;
    }

    //y축에 대해서, axis 에는 Vector3(0, 0, 1);
    public static Vector3 rotateVector3ByAngleAxis(Vector3 source, Vector3 axis, double degreeAngle){
        double x, y, z; double u, v, w;
        double radians = Math.toRadians(degreeAngle);
        x=source.x(); y=source.y(); z=source.z();
        u=axis.x(); v=axis.y(); w=axis.z();
        double xPrime = u*(u*x + v*y + w*z)*(1d - Math.cos(radians))
                + x*Math.cos(radians)
                + (-w*y + v*z)*Math.sin(radians);
        double yPrime = v*(u*x + v*y + w*z)*(1d - Math.cos(radians))
                + y*Math.cos(radians)
                + (w*x - u*z)*Math.sin(radians);
        double zPrime = w*(u*x + v*y + w*z)*(1d - Math.cos(radians))
                + z*Math.cos(radians)
                + (-v*x + u*y)*Math.sin(radians);
        return new Vector3( (float)xPrime, (float)yPrime, (float)zPrime );
    }


    public Vector3 normalize()
    {
        float length = length();

        this.x = this.x/length; // dx /= length;
        this.y = this.y/length; // dy /= length;
        this.z = this.z/length; // dz /= length;

        //source 방향을 가리키는, 크기가 1인 벡터를 반환.
        Vector3 normalizeVec = new Vector3(this.x, this.y, this.z);

        return  normalizeVec;
    }


    public static Vector3 getTargetDirection (Vector3 source, Vector3 target)
    {
        float x = target.x- source.x;
        float y = target.y- source.y;
        float z = target.z- source.z;
        return new Vector3(x,y,z);
    }

    public static void movePosition(Vector3 source, Vector3 movement)
    {
        source.x(source.x() + movement.x());
        source.y(source.y() + movement.y());
        source.z(source.z() + movement.z());
        //return source;

    }

    @Override
    public Object clone() {

        Vector3 vector3;
        try {
            vector3 = (Vector3) super.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vector3;
    }
}