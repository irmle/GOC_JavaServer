package ECS.Classes;

public class Vector2 implements Cloneable {

    private float x = 0f;
    private float y = 0f;

    public static final Vector2 zero = new Vector2(0f,0f);

    public Vector2()
    {
        this.x = 0f;
        this.y = 0f;
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }


    public float x()
    {
        return this.x;
    }

    public float y()
    {
        return this.y;
    }

    public void x(float x)
    {
        this.x = x;
    }

    public void y(float y)
    {
        this.y = y;
    }

    public Vector2 setSpeed(float speed)
    {
        this.x *= speed;
        this.y *= speed;
        return this;
    }

    public Vector2 setSpeed(int speed)
    {
        this.x *= speed;
        this.y *= speed;
        return this;
    }

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 target)
    {
        this.x = target.x;
        this.y = target.y;
    }

    //내적 구하기
    public static float dot(Vector2 source, Vector2 target)
    {
        float dot = source.x * target.x + source.y * target.y;
        return dot;
    }

    //두 벡터 사이의 사이각 구하기 0~180도
    public static float getAngle(Vector2 source, Vector2 target)
    {
        float dot = dot(source, target);
        float length = source.length() * target.length();
        float calc = dot / length;

        //-1f ~ 1f의 범위를 넘어가면 안된다.
        if(calc > 1f)
            calc = 1f;
        else if(calc < -1f)
            calc = -1f;

        return (float) Math.toDegrees( Math.acos(calc) );
    }

    //제곱근 연산을 진행함.
    public static float distance(Vector2 source, Vector2 target)
    {
        float dx = target.x - source.x;
        float dy = target.y - source.y;

        float result = (float) Math.sqrt(dx*dx + dy*dy);

        if(result<0) //음수일 경우, 양수로 만들어 줄 것.
            result *= -1f;

        return result;
    }

    //제곱근 연산을 제외함.
    public static float sqrMagnitudeDistance(Vector2 source, Vector2 target)
    {
        float dx = target.x - source.x;
        float dy = target.y - source.y;

        float result = (dx*dx + dy*dy);

        if(result<0) //음수일 경우, 양수로 만들어 줄 것.
            result *= -1f;

        return result;
    }

    public float length()
    {
        float x = this.x;
        float y = this.y;
        float length = (float) Math.sqrt(x*x + y*y);

        return length;
    }

    public static Vector2 normalizeVector(Vector2 source, Vector2 target)
    {
        float length = Vector2.distance(source, target);
        float dx = target.x - source.x;
        float dy = target.y - source.y;

        dx = dx/length; // dx /= length;
        dy = dy/length; // dy /= length;

        //source > target방향을 가리키는, 크기가 1인 벡터를 반환.
        Vector2 nomalizeVec = new Vector2(dx, dy);

        return  nomalizeVec;
    }


    public static Vector2 rotateVector2ByAngleAxis(Vector2 vector, double angle) {

        double radians = Math.toRadians(angle);

        float x1 = (float)(vector.x * Math.cos(radians) - vector.y * Math.sin(radians));

        float y1 = (float)(vector.x * Math.sin(radians) + vector.y * Math.cos(radians));

        return new Vector2(x1, y1);
    }


    public Vector2 normalize()
    {
        float length = length();

        this.x = this.x/length; // dx /= length;
        this.y = this.y/length; // dy /= length;

        //source 방향을 가리키는, 크기가 1인 벡터를 반환.
        Vector2 nomalizeVec = new Vector2(this.x, this.y);

        return  nomalizeVec;
    }


    public void movePosition(Vector2 source, Vector2 movement)
    {
        source.x(source.x() + movement.x());
        source.y(source.y() + movement.y());
        //return source;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        Vector2 vector2;
        try {
            vector2 = (Vector2) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return vector2;
    }
}
