package nickhartung.libgdx.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import nickhartung.libgdx.utilities.AllocationGuard;
import nickhartung.libgdx.utilities.BaseObject;
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

    private RenderElementPool  mElementPool;
    private DeferredObjectPool mDeferredPool;
    private ObjectManager[] mRenderQueues;
    private FixedSizeArray<DeferredObject> mDeferredQueue;
    private int mQueueIndex;

    private static SpriteBatch mSpriteBatch;
    private static ShapeRenderer mShapeRenderer;

    public RenderSystem( final int pNumDrawQueues, final int pQueueSize ) {
        super();
        this.mElementPool = new RenderElementPool( pNumDrawQueues * pQueueSize );
        this.mDeferredPool = new DeferredObjectPool( pQueueSize );
        this.mDeferredQueue = new FixedSizeArray<DeferredObject>( pQueueSize );
        this.mRenderQueues = new ObjectManager[ pNumDrawQueues ];
        for( int i = 0; i < pNumDrawQueues; i++ ) {
            this.mRenderQueues[ i ] = new PhasedObjectManager( pQueueSize );
        }
        this.mQueueIndex = 0;
    }

    public RenderSystem() {
        this( NUM_DRAW_QUEUES, QUEUE_SIZE );
    }

    public static void setRenderers( final SpriteBatch pSpriteBatch, final ShapeRenderer pShapeRenderer ) {
        RenderSystem.mSpriteBatch   = pSpriteBatch;
        RenderSystem.mShapeRenderer = pShapeRenderer;
    }

    public void scheduleForDraw( final DrawableObject pObject, final Vector2 pPosition, final int pPriority, final boolean pDeferred ) {
        if( pDeferred ) {
            DeferredObject deferred = this.mDeferredPool.allocate();
            if( deferred != null ) {
                deferred.set( pObject, pPosition, pPriority );
                this.mDeferredQueue.add( deferred );
            }
        } else {
            RenderElement element = this.mElementPool.allocate();
            if( element != null ) {
                element.set( pObject, pPosition, pPriority );
                this.mRenderQueues[ this.mQueueIndex ].add( element );
            }
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
        // Submit any objects whose rendering was deferred
        this.submitDeferredObjects();

        this.mRenderQueues[ this.mQueueIndex ].commitUpdates();

        // This code will block if the previous queue is still being executed.
        pRenderer.setDrawQueue( this.mRenderQueues[ this.mQueueIndex ], pCamera );

        final int lastQueue = ( this.mQueueIndex == 0 ) ? NUM_DRAW_QUEUES - 1 : this.mQueueIndex - 1;

        // Clear the old queue.
        FixedSizeArray<BaseObject> objects = this.mRenderQueues[ lastQueue ].getObjects();
        clearQueue( objects );

        this.mQueueIndex = ( this.mQueueIndex + 1 ) % NUM_DRAW_QUEUES;
    }

    private void submitDeferredObjects() {
        final int numDeferred = this.mDeferredQueue.getCount();
        Object[] objects = this.mDeferredQueue.getArray();
        final DeferredObjectPool objectPool  = this.mDeferredPool;
        final RenderElementPool  elementPool = this.mElementPool;

        for( int i = numDeferred - 1; i >= 0; i-- ) {
            final DeferredObject deferredObject = (DeferredObject)objects[ i ];
            RenderElement element = elementPool.allocate();
            if( element != null ) {
                element.set( deferredObject.drawable, deferredObject.position, deferredObject.priority );
                this.mRenderQueues[ this.mQueueIndex ].add( element );
            }
            objectPool.release( deferredObject );
            this.mDeferredQueue.removeLast();
        }
    }

    public static SpriteBatch getSpriteBatch() {
        return RenderSystem.mSpriteBatch;
    }

    public static ShapeRenderer getShapeRenderer() {
        return RenderSystem.mShapeRenderer;
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

    protected class DeferredObjectPool extends TObjectPool<DeferredObject> {

        DeferredObjectPool( final int max ) {
            super( max );
        }

        @Override
        public void release( final Object element ) {
            final DeferredObject deferred = (DeferredObject)element;
            // reset on release
            deferred.reset();
            super.release( element );
        }

        @Override
        protected void fill() {
            for( int x = 0; x < getSize(); x++ ) {
                this.getAvailable().add( new DeferredObject() );
            }
        }
    }

    protected class DeferredObject extends AllocationGuard {
        public DrawableObject drawable;
        public Vector2        position;
        public int            priority;

        public DeferredObject() {
            super();
        }

        public void set( final DrawableObject pDrawable, final Vector2 pPosition, final int pPriority ) {
            this.drawable = pDrawable;
            this.position = pPosition;
            this.priority = pPriority;
        }

        public void reset() {
            this.drawable = null;
            this.position = null;
            this.priority = 0;
        }
    }
}
