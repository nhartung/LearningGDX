package nickhartung.learninggdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GDXTop extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

    private OrthographicCamera mCamera;
    private FitViewport mViewport;
    private Sprite mSprite;
	
	@Override
	public void create () {
        this.mCamera   = new OrthographicCamera();
        this.mViewport = new FitViewport( 960.0f, 540.0f, this.mCamera );
        this.mViewport.apply();

        this.mCamera.position.set( this.mCamera.viewportWidth / 2, this.mCamera.viewportHeight / 2, 0 );

		batch = new SpriteBatch();
		this.mSprite = new Sprite( new Texture("badlogic.jpg") );
        this.mSprite.setPosition( 0.0f, 0.0f );
        this.mSprite.setSize( 100.0f, 100.0f );
	}

	@Override
	public void render () {
        this.mCamera.update();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix( this.mCamera.combined );
		batch.begin();
		this.mSprite.draw( batch );
		batch.end();
	}

    @Override
    public void resize (int width, int height) {
        this.mViewport.update(width, height);
        this.mCamera.position.set( this.mCamera.viewportWidth / 2, this.mCamera.viewportHeight / 2, 0 );
    }
}
