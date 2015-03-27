package nickhartung.learninggdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.libgdx.utilities.DrawableObject;
import nickhartung.libgdx.utilities.ObjectManager;
import nickhartung.libgdx.utilities.PhasedObject;
import nickhartung.libgdx.utilities.PhasedObjectManager;
import nickhartung.libgdx.utilities.TObjectPool;
import nickhartung.utilities.FixedSizeArray;

/**
 * Created by Nick on 3/16/2015.
 */
public class RenderSystem {

    private static final int NUM_DRAW_QUEUES = 2;
    private static final int QUEUE_SIZE = 50;

    private RenderElementPool mElementPool;
    private ObjectManager[] mRenderQueues;
    private int mQueueIndex;

    private Camera mCamera;
    private SpriteBatch mSpriteBatch;
    private ShapeRenderer mShapeRenderer;

    public RenderSystem() {
        super();
        this.mElementPool = new RenderElementPool( NUM_DRAW_QUEUES * QUEUE_SIZE );
        this.mRenderQueues = new ObjectManager[ NUM_DRAW_QUEUES ];
        for( int i = 0; i < NUM_DRAW_QUEUES; i++ ) {
            this.mRenderQueues[ i ] = new PhasedObjectManager( QUEUE_SIZE );
        }
        this.mQueueIndex = 0;
    }

    public void scheduleForDraw( final DrawableObject object, final Vector2 position, final int priority ) {
        RenderElement element = this.mElementPool.allocate();
        if( element != null ) {
            element.set( object, position, priority );
            this.mRenderQueues[ this.mQueueIndex ].add( element );
        }
    }

    private void clearQueue( final FixedSizeArray<BaseObject> objects ) {
        final int count = objects.getCount();
        final Object[] objectArray = objects.getArray();
        final RenderElementPool elementPool = this.mElementPool;
        for( int i = count - 1; i >= 0; i-- ) {
            RenderElement element = (RenderElement)objectArray[ i ];
            elementPool.release( element );
            objects.removeLast();
        }
    }

    public void swap( final Renderer pRenderer, final Camera pCamera ) {
        this.mRenderQueues[ this.mQueueIndex ].commitUpdates();

        // This code will block if the previous queue is still being executed.
        pRenderer.setDrawQueue( this.mRenderQueues[ this.mQueueIndex ], pCamera, this.mSpriteBatch, this.mShapeRenderer );

        final int lastQueue = ( this.mQueueIndex == 0 ) ? NUM_DRAW_QUEUES - 1 : this.mQueueIndex - 1;

        // Clear the old queue.
        FixedSizeArray<BaseObject> objects = this.mRenderQueues[ lastQueue ].getObjects();
        clearQueue( objects );

        this.mQueueIndex = ( this.mQueueIndex + 1 ) % NUM_DRAW_QUEUES;
    }

    public void setCamera( final Camera pCamera ) {
        this.mCamera = pCamera;
    }

    public Camera getCamera() {
        return this.mCamera;
    }

    public void setSpriteBatch( final SpriteBatch pBatch ) {
        this.mSpriteBatch = pBatch;
    }

    public void setShapeRenderer( final ShapeRenderer pShapeRenderer ) {
        this.mShapeRenderer = pShapeRenderer;
    }

    public SpriteBatch getSpriteBatch() {
        return this.mSpriteBatch;
    }

    public ShapeRenderer getShapeRenderer() {
        return this.mShapeRenderer;
    }

    public class RenderElement extends PhasedObject {

        public DrawableObject mDrawable;
        public float x;
        public float y;

        public RenderElement() {
            super();
        }

        public void set( final DrawableObject drawable, final Vector2 position, final int priority ) {
            this.mDrawable = drawable;
            x = position.x;
            y = position.y;
            this.setPhase( priority * 100 + this.mDrawable.getType().ordinal() );
        }

        public void reset() {
            mDrawable = null;
            x = 0.0f;
            y = 0.0f;
        }
    }

    protected class RenderElementPool extends TObjectPool<RenderElement> {

        RenderElementPool( final int max ) {
            super( max );
        }

        @Override
        public void release( final Object element ) {
            final RenderElement renderable = (RenderElement)element;
            // reset on release
            renderable.reset();
            super.release( element );
        }

        @Override
        protected void fill() {
            for( int x = 0; x < getSize(); x++ ) {
                this.getAvailable().add( new RenderElement() );
            }
        }
    }
}
