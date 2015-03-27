package nickhartung.learninggdx;

/**
 * Created by Nick on 3/24/2015.
 */
public class InputSystem {

    private boolean mMoveUp;
    private boolean mMoveDown;
    private boolean mMoveLeft;
    private boolean mMoveRight;

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
}
