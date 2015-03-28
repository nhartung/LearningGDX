package nickhartung.learninggdx;

/**
 * Created by Nick on 3/24/2015.
 */
public class InputSystem {

    private boolean mMoveUp;
    private boolean mMoveDown;
    private boolean mMoveLeft;
    private boolean mMoveRight;

    private boolean mA;
    private boolean mR;
    private boolean mG;
    private boolean mB;
    private boolean mV;

    public InputSystem() {

    }

    public boolean moveUp() {
        return this.mMoveUp;
    }

    public boolean moveDown() {
        return this.mMoveDown;
    }

    public boolean moveLeft() {
        return this.mMoveLeft;
    }

    public boolean moveRight() {
        return this.mMoveRight;
    }

    public void setMoveUp( final boolean pMove ) {
        this.mMoveUp = pMove;
    }

    public void setMoveDown( final boolean pMove ) {
        this.mMoveDown = pMove;
    }

    public void setMoveLeft( final boolean pMove ) {
        this.mMoveLeft = pMove;
    }

    public void setMoveRight( final boolean pMove ) {
        this.mMoveRight = pMove;
    }

    public void setR( final boolean pIn ) {
        this.mR = pIn;
    }

    public void setG( final boolean pIn ) {
        this.mG = pIn;
    }

    public void setB( final boolean pIn ) {
        this.mB = pIn;
    }

    public boolean getR() {
        return this.mR;
    }

    public boolean getG() {
        return this.mG;
    }

    public boolean getB() {
        return this.mB;
    }

    public void setV( final boolean pIn ) {
        this.mV = pIn;
    }

    public boolean getV() {
        return this.mV;
    }

    public void setA( final boolean pIn ) {
        this.mA = pIn;
    }

    public boolean getA() {
        return this.mA;
    }
}
