package nickhartung.learninggdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

import nickhartung.libgdx.utilities.BoxShapeDrawable;
import nickhartung.libgdx.utilities.CircleShapeDrawable;
import nickhartung.libgdx.utilities.ObjectManager;
import nickhartung.libgdx.utilities.SpriteDrawable;

public class GDXTop extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

    private OrthographicCamera mCamera;
    private FitViewport mViewport;
    private Sprite mSprite;
    private Renderer mRenderer;
    private SpriteBatch mSpriteBatch;
    private ShapeRenderer mShapeRenderer;

    private GameObject test;
    private GameObject test2;
    private GameObject test3;
    private GameObject test4;
    private ObjectManager manager;
	
	@Override
	public void create () {
        this.mCamera   = new OrthographicCamera();
        this.mViewport = new FitViewport( 960.0f, 540.0f, this.mCamera );
        this.mViewport.apply();

        this.mCamera.position.set(this.mCamera.viewportWidth / 2, this.mCamera.viewportHeight / 2, 0);

		this.mSprite = new Sprite( new Texture("badlogic.jpg") );
        this.mSprite.setPosition( 0.0f, 0.0f );
        this.mSprite.setSize(100.0f, 100.0f);

        this.mRenderer = new Renderer();
        this.mSpriteBatch = new SpriteBatch();
        this.mShapeRenderer = new ShapeRenderer();

        RenderSystem render = new RenderSystem();
        render.setCamera( this.mCamera );
        render.setSpriteBatch(this.mSpriteBatch);
        render.setShapeRenderer( this.mShapeRenderer );
        ObjectRegistry.sRenderSystem = render;

        ///////////////////
        Sprite sprite1 = new Sprite( new Texture("badlogic.jpg") );
        sprite1.setSize(100.0f, 100.0f);
        RenderComponent renderCom = new RenderComponent();
        SpriteDrawable spriteDrawable = new SpriteDrawable();
        spriteDrawable.setSprite( sprite1 );
        renderCom.setDrawable(spriteDrawable);
        renderCom.setPriority( 1 );

        Color red = new Color();
        red.set( 1.0f, 0.0f, 0.0f, 1.0f );

        Color blue = new Color();
        blue.set( 0.0f, 0.0f, 1.0f, 1.0f );

        BoxShapeDrawable box = new BoxShapeDrawable();
        box.set( 0.0f, 100.0f, 100.0f, 0.0f );
        box.setColor(red);
        box.setShapeType( ShapeRenderer.ShapeType.Line );
        Vector2 pos = new Vector2();
        pos.set( 150.0f, 150.0f );

        CircleShapeDrawable circle = new CircleShapeDrawable();
        circle.set( 75.0f, 50 );
        circle.setColor(blue);
        circle.setShapeType( ShapeRenderer.ShapeType.Filled );
        Vector2 pos2 = new Vector2();
        pos2.set( 100.0f, 100.0f );

        RenderComponent boxRenderer = new RenderComponent();
        boxRenderer.setPriority( 0 );
        boxRenderer.setDrawable( box );

        RenderComponent circleRenderer = new RenderComponent();
        circleRenderer.setPriority( 0 );
        circleRenderer.setDrawable( circle );

        MovementComponent movement = new MovementComponent();
        movement.shared = true;

        TestMovementComponent testComp = new TestMovementComponent();
        testComp.setMovementSpeed( 100.0f, 20.0f );

        test = new GameObject();
        test.add( renderCom );
        test.add( movement );
        test.add( testComp );
        test.setPosition( test.getPosition().add( 0.0f, 0.0f ) );

        test2 = new GameObject();
        test2.add( renderCom );
        test2.setPosition( test2.getPosition().add( 100.0f, 100.0f ) );

        test3 = new GameObject();
        test3.add( boxRenderer );
        test3.setPosition( test3.getPosition().add( 150.0f, 150.0f ) );

        test4 = new GameObject();
        test4.add( circleRenderer );
        test4.setPosition( test4.getPosition().add( 100.0f, 100.0f ) );

        manager = new ObjectManager( 64 );
        manager.add( test );
        manager.add( test2 );
        manager.add( test3 );
        manager.add( test4 );

	}

	@Override
	public void render() {
        this.mCamera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        final RenderSystem renderSystem = ObjectRegistry.sRenderSystem;

        manager.update( Gdx.graphics.getDeltaTime(), null );
        renderSystem.swap( mRenderer, mCamera );
        mRenderer.render();
	}

    @Override
    public void resize (int width, int height) {
        this.mViewport.update(width, height);
        this.mCamera.position.set( this.mCamera.viewportWidth / 2, this.mCamera.viewportHeight / 2, 0 );
    }
}
