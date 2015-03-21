package nickhartung.learninggdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.libgdx.utilities.DrawableObject;
import nickhartung.libgdx.utilities.ObjectManager;
import nickhartung.utilities.FixedSizeArray;

/**
 * Created by Nick on 3/19/2015.
 */
public class Renderer {

    private ObjectManager mDrawQueue;
    private Camera        mCamera;
    private SpriteBatch   mSpriteBatch;
    private ShapeRenderer mShapeRenderer;

    public void render() {
        final RenderSystem renderSystem = ObjectRegistry.sRenderSystem;

        this.mSpriteBatch.setProjectionMatrix(   this.mCamera.combined );
        this.mShapeRenderer.setProjectionMatrix( this.mCamera.combined );

        DrawableObject.DrawableType lastType = DrawableObject.DrawableType.NoType;
        final FixedSizeArray<BaseObject> drawables = this.mDrawQueue.getObjects();
        Object[] objectArray = drawables.getArray();
        final int count = drawables.getCount();
        for( int i = 0; i < count; i++ ) {
            final RenderSystem.RenderElement element = (RenderSystem.RenderElement)objectArray[ i ];
            final DrawableObject.DrawableType thisType = element.mDrawable.getType();
            if( thisType != lastType ) {
                this.endDrawing( lastType );
                this.beginDrawing( thisType );
                lastType = thisType;
            }
            element.mDrawable.draw( element.x, element.y );
        }
        this.endDrawing(lastType);
    }

    public void setDrawQueue( final ObjectManager queue, final Camera pCamera, final SpriteBatch pBatch, final ShapeRenderer pShapeRenderer ) {
        this.mDrawQueue     = queue;
        this.mCamera        = pCamera;
        this.mSpriteBatch   = pBatch;
        this.mShapeRenderer = pShapeRenderer;
    }

    private void beginDrawing( final DrawableObject.DrawableType pType ) {
        switch( pType ) {
            case Sprite:
                this.mSpriteBatch.begin();
                break;
            case ShapeFilled:
                this.mShapeRenderer.begin( ShapeRenderer.ShapeType.Filled );
                break;
            case ShapeLine:
                this.mShapeRenderer.begin( ShapeRenderer.ShapeType.Line );
                break;
            case ShapePoint:
                this.mShapeRenderer.begin( ShapeRenderer.ShapeType.Point );
                break;
            default:
            case Mesh:
            case NoType:
                // Do nothing
                break;
        }
    }

    private void endDrawing( final DrawableObject.DrawableType pType ) {
        switch( pType ) {
            case Sprite:
                this.mSpriteBatch.end();
                break;
            case ShapeFilled:
            case ShapeLine:
            case ShapePoint:
                this.mShapeRenderer.end();
                break;
            default:
            case Mesh:
            case NoType:
                // Do nothing
                break;
        }
    }
}
