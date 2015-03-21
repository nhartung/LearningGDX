package nickhartung.learninggdx;

/**
 * Created by Nick on 3/21/2015.
 */
public abstract class RenderComponent extends GameComponent {
    protected int mPriority;

    public RenderComponent() {
        super();
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        this.mPriority = 0;
    }

    public void setPriority( final int pPriority ) {
        this.mPriority = pPriority;
    }
}
