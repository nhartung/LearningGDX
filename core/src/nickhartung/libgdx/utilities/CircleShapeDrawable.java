package nickhartung.libgdx.utilities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import nickhartung.learninggdx.ObjectRegistry;

/**
 * Created by Nick on 3/21/2015.
 */
public class CircleShapeDrawable extends ShapeDrawable {

    public CircleShapeDrawable() {
        this.mType = DrawableObject.DrawableType.ShapeFilled;
    }

    @Override
    public void draw( final float x, final float y ) {
        assert ( this.mColor != null );
        final ShapeRenderer shapeRenderer = ObjectRegistry.sRenderSystem.getShapeRenderer();
        shapeRenderer.setColor( this.mColor );
        shapeRenderer.circle( x, y, this.mData[ DataItems.DataRadius.getValue() ], (int)this.mData[ DataItems.DataSegments.getValue() ] );
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void set( final float pRadius, int pSegments ) {
        this.mData[ ShapeDrawable.DataItems.DataRadius.getValue() ] = pRadius;
        this.mData[ ShapeDrawable.DataItems.DataSegments.getValue() ] = pSegments;
    }
}
