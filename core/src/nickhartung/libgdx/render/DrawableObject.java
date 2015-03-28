package nickhartung.libgdx.render;

import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 3/16/2015.
 */
public abstract class DrawableObject extends BaseObject {

    protected DrawableType mType;

    public enum DrawableType {
        Sprite,
        Mesh,
        ShapeFilled,
        ShapeLine,
        ShapePoint,
        NoType
    }

    abstract public void draw( final float x, final float y );

    public DrawableObject() {
        this.mType = DrawableType.NoType;
    }

    @Override
    public void reset() {
        this.mType = DrawableType.NoType;
    }

    public DrawableType getType() {
        return this.mType;
    }
}
