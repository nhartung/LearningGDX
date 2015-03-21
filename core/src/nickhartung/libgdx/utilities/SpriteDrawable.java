package nickhartung.libgdx.utilities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nickhartung.learninggdx.ObjectRegistry;

/**
 * Created by Nick on 3/19/2015.
 */
public class SpriteDrawable extends DrawableObject {

    private Sprite mSprite;

    public SpriteDrawable() {
        this.mType = DrawableType.Sprite;
    }

    @Override
    public void draw( final float x, final float y ) {
        assert( mSprite != null );
        SpriteBatch batch = ObjectRegistry.sRenderSystem.getSpriteBatch();
        this.mSprite.setPosition( x, y );
        this.mSprite.draw( batch );
    }

    @Override
    public void reset() {
        this.mSprite = null;
    }

    public void setSprite( final Sprite pSprite ) {
        this.mSprite = pSprite;
    }

    public Sprite getSprite() {
        return this.mSprite;
    }
}
