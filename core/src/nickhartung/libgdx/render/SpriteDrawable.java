package nickhartung.libgdx.render;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Nick on 3/19/2015.
 */
public class SpriteDrawable extends DrawableObject {

    private Sprite mSprite;

    public SpriteDrawable() {
        super();
        this.mType = DrawableType.Sprite;
    }

    @Override
    public void draw( final float x, final float y ) {
        assert ( this.mSprite != null );
        final SpriteBatch spriteBatch = RenderSystem.getSpriteBatch();
        this.mSprite.setPosition( x, y );
        this.mSprite.draw( spriteBatch );
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
