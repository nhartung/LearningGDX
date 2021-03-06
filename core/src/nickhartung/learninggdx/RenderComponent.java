package nickhartung.learninggdx;

import nickhartung.libgdx.render.DrawableObject;
import nickhartung.libgdx.render.RenderSystem;
import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 3/21/2015.
 */
public class RenderComponent extends GameComponent {

    protected int mPriority;
    protected DrawableObject mDrawable;
    protected boolean mDeferred;

    public RenderComponent() {
        super();
        this.mDrawable = null;
        this.mDeferred = false;
        this.mPriority = 0;
        this.setPhase( ComponentPhases.DRAW.ordinal() );
    }

    @Override
    public void update( float timeDelta, final BaseObject parent ) {
        if( this.mDrawable != null ) {
            RenderSystem renderSystem = ObjectRegistry.renderSystem;
            if( renderSystem != null ) {
                final GameObject gameObject = (GameObject)parent;
                renderSystem.scheduleForDraw( this.mDrawable, gameObject.getPosition(), this.mPriority, this.mDeferred );
            }
        }
    }

    public void setDrawable( final DrawableObject pDrawable ) {
        this.mDrawable = pDrawable;
    }

    @Override
    public void reset() {
        super.reset();
        this.mPriority = 0;
        this.mDeferred = false;
        this.mDrawable = null;
    }

    public void setPriority( final int pPriority ) {
        this.mPriority = pPriority;
    }

    public void setDeferred( final boolean pDeferred ) {
        this.mDeferred = pDeferred;
    }
}
