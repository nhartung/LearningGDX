package nickhartung.libgdx.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Nick on 3/21/2015.
 */
public class BoxShapeDrawable extends ShapeDrawable {

    public BoxShapeDrawable() {
        super();
        this.mType = DrawableType.ShapeFilled;
    }

    @Override
    public void draw( final float x, final float y ) {
        assert ( this.mColor != null );
        final ShapeRenderer shapeRenderer = RenderSystem.getShapeRenderer();
        shapeRenderer.setColor( this.mColor );
        shapeRenderer.box( x, y, this.mData[ DataItems.DataZ.getValue() ], this.mData[ DataItems.DataWidth.getValue() ],
                this.mData[ DataItems.DataHeight.getValue() ], this.mData[ DataItems.DataDepth.getValue() ] );
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void set( final float pZ, float pWidth, float pHeight, float pDepth ) {
        this.mData[ DataItems.DataZ.getValue() ] = pZ;
        this.mData[ DataItems.DataWidth.getValue() ] = pWidth;
        this.mData[ DataItems.DataHeight.getValue() ] = pHeight;
        this.mData[ DataItems.DataDepth.getValue() ] = pDepth;
    }
}
