package nickhartung.libgdx.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Nick on 3/21/2015.
 */
public abstract class ShapeDrawable extends DrawableObject {
    public enum DataItems {
        DataZ( 0 ),
        DataWidth( 1 ),
        DataHeight( 2 ),
        DataDepth( 3 ),
        DataRadius( 4 ),
        DataSegments( 5 ),
        NumDataItems( 6 );

        private final int mId;

        DataItems( final int pId ) {
            this.mId = pId;
        }

        public int getValue() {
            return this.mId;
        }
    }

    protected float[] mData;
    protected Color mColor;

    public ShapeDrawable() {
        super();
        mData = new float[ DataItems.NumDataItems.getValue() ];
    }

    @Override
    public void reset() {
        super.reset();
        for( int i = 0; i < DataItems.NumDataItems.getValue(); i++ ) {
            this.mData[ i ] = 0.0f;
        }
    }

    public void setShapeType( final ShapeRenderer.ShapeType pShapeType ) {
        switch( pShapeType ) {
            case Line:
                this.mType = DrawableType.ShapeLine;
                break;
            case Point:
                this.mType = DrawableType.ShapePoint;
                break;
            case Filled:
            default:
                this.mType = DrawableType.ShapeFilled;
                break;
        }
    }

    public void setColor( final Color pColor ) {
        this.mColor = pColor;
    }
}
