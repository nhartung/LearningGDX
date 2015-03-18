package nickhartung.learninggdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nickhartung.utilities.FixedSizeArray;

/**
 * Created by Nick on 3/16/2015.
 */
public class RenderSystem {

    private static final int NUM_DRAW_QUEUES = 2;
    private static final int QUEUE_SIZE = 50;

    private FixedSizeArray<Sprite>[] mRenderQueues;
    private int mQueueIndex;
    private SpriteBatch mSpriteBatch;
    private Camera mCamera;

    public RenderSystem() {
        super();
        this.mRenderQueues = new FixedSizeArray[ NUM_DRAW_QUEUES ];
        for( int i = 0; i < NUM_DRAW_QUEUES; i++ ) {
            this.mRenderQueues[ i ] = new FixedSizeArray<Sprite>( QUEUE_SIZE );
        }
        this.mQueueIndex = 0;

        this.mSpriteBatch = new SpriteBatch();
    }

    public void scheduleForDraw( final Sprite pSprite ) {
        this.mRenderQueues[ mQueueIndex ].add( pSprite );
    }

    public void render() {
        this.mSpriteBatch.setProjectionMatrix( this.mCamera.combined );
        this.mSpriteBatch.begin();
        final Object[] sprites = this.mRenderQueues[ mQueueIndex ].getArray();
        for( int i = 0; i < this.mRenderQueues[ mQueueIndex ].getCount(); i++ ) {
            final Sprite sprite = (Sprite)sprites[ i ];
            sprite.draw( this.mSpriteBatch );
        }
        this.mSpriteBatch.end();
    }

    public void swap() {
        this.mRenderQueues[ mQueueIndex ].clear();
        mQueueIndex = (mQueueIndex + 1) % NUM_DRAW_QUEUES;
    }

    public void setCamera( final Camera pCamera ) {
        this.mCamera = pCamera;
    }
}
