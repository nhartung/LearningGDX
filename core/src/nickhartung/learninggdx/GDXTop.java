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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

import box2dLight.Light;
import box2dLight.RayHandler;
import nickhartung.learninggdx.physics.box2d.Box2DMovementComponent;
import nickhartung.learninggdx.physics.box2d.Box2DSystem;
import nickhartung.learninggdx.physics.box2d.Box2DWorldComponent;
import nickhartung.learninggdx.physics.standalone.MovementComponent;
import nickhartung.libgdx.render.BoxShapeDrawable;
import nickhartung.libgdx.render.RenderSystem;
import nickhartung.libgdx.render.Renderer;
import nickhartung.libgdx.render.SpriteDrawable;
import nickhartung.libgdx.utilities.ObjectManager;

public class GDXTop extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    private OrthographicCamera mCamera;
    private FitViewport mViewport;
    private Sprite mSprite;
    private Renderer mRenderer;
    private SpriteBatch mSpriteBatch;
    private ShapeRenderer mShapeRenderer;

    private ObjectManager mManager;

    private float      mModifier;
    private boolean    mlastV;
    private Body       mBody;
    private Light pt;

    private Body testBod;

    private Box2DDebugRenderer mDebugRenderer;

    public static float PIXELS_PER_METER = 100.0f;

    @Override
    public void create() {
        this.mCamera = new OrthographicCamera();
        this.mViewport = new FitViewport( 960.0f, 540.0f, this.mCamera );
        this.mViewport.apply();

        this.mCamera.position.set( this.mCamera.viewportWidth / 2, this.mCamera.viewportHeight / 2, 0 );

        this.mRenderer = new Renderer();
        this.mSpriteBatch = new SpriteBatch();
        this.mShapeRenderer = new ShapeRenderer();

        RenderSystem render = new RenderSystem();
        render.setRenderers( this.mSpriteBatch, this.mShapeRenderer );
        ObjectRegistry.renderSystem = render;

        Box2DSystem box2DSystem = new Box2DSystem( new Vector2( 0, 0 ), false, 6, 2 );
        ObjectRegistry.box2DSystem = box2DSystem;
        mDebugRenderer = new Box2DDebugRenderer();

        ObjectRegistry.box2DSystem = box2DSystem;
        RayHandler rayHandler = new RayHandler( box2DSystem.getWorld() );
        rayHandler.setCulling( true );
        rayHandler.setShadows( true );
        rayHandler.useDiffuseLight( true );
        rayHandler.setAmbientLight( 1.0f, 1.0f, 1.0f, 1.0f );
        rayHandler.setBlur( false );

        InputSystem inputSystem = new InputSystem();
        ObjectRegistry.inputSystem = inputSystem;

        // Box2d ~~~~~~~~~~~~~~~~~~~~~~
        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.DynamicBody;
        groundBodyDef.position.set( 0.0f, 100.0f / PIXELS_PER_METER );
        groundBodyDef.linearDamping = 0.0f;
        groundBodyDef.angularDamping = 0.0f;

        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox( 50.0f / PIXELS_PER_METER, 50.0f / PIXELS_PER_METER );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.density = 0.0f;
        fixtureDef.isSensor = true;

        this.mBody = box2DSystem.createBody( groundBodyDef, fixtureDef );

        GameObject backgroundObject = new GameObject();
        Sprite backgroundSprite = new Sprite( new Texture( "background.jpg" ) );
        SpriteDrawable backgroundDrawable = new SpriteDrawable();
        backgroundDrawable.setSprite( backgroundSprite );
        RenderComponent backgroundRenderComponent = new RenderComponent();
        backgroundRenderComponent.setDrawable( backgroundDrawable );
        backgroundObject.add( backgroundRenderComponent );

        GameObject standaloneObject = new GameObject();
        BoxShapeDrawable boxDrawable = new BoxShapeDrawable();
        boxDrawable.set( 0.0f, 100.0f, 100.0f, 0.0f );
        boxDrawable.setColor( Color.RED );
        RenderComponent standaloneRenderComponent = new RenderComponent();
        standaloneRenderComponent.setDrawable( boxDrawable );
        DumbMovementComponent dumbMoverStandalone = new DumbMovementComponent();
        dumbMoverStandalone.setMovementSpeed( 100.0f, 100.0f );
        MovementComponent movementStandalone = new MovementComponent();
        standaloneObject.add( dumbMoverStandalone );
        standaloneObject.add( movementStandalone );
        standaloneObject.add( standaloneRenderComponent );

        GameObject box2DObject = new GameObject();
        RenderComponent box2DRenderComponent = new RenderComponent();
        box2DRenderComponent.setDrawable( boxDrawable );
        DumbMovementComponent dumbMoverBox2D = new DumbMovementComponent();
        dumbMoverBox2D.setMovementSpeed( 100.0f / PIXELS_PER_METER, 100.0f / PIXELS_PER_METER );
        Box2DMovementComponent box2DMover = new Box2DMovementComponent();
        box2DMover.setBody( this.mBody );

        box2DObject.add( box2DRenderComponent );
        box2DObject.add( dumbMoverBox2D );
        box2DObject.add( box2DMover );
        box2DObject.getPosition().set( 0.0f, 100.0f );

        GameObject worldObject = new GameObject();
        Box2DWorldComponent worldCom = new Box2DWorldComponent();
        worldCom.setWorld( world );
        worldObject.add( worldCom );

        this.mManager = new ObjectManager( 64 );
        this.mManager.add( backgroundObject );
        this.mManager.add( standaloneObject );
        this.mManager.add( box2DObject );
        this.mManager.add( worldObject );
    }

    @Override
    public void render() {
        this.mCamera.update();

        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        final RenderSystem renderSystem = ObjectRegistry.renderSystem;
        final RayHandler rayHandler = ObjectRegistry.rayHandler;
        final InputSystem inputSystem = ObjectRegistry.inputSystem;

        final float delta = Gdx.graphics.getDeltaTime();

        this.mManager.update( 1.0f / 60.0f, null );

        System.out.println( "Box2D: ---------------" );
        System.out.println( "Velocity X: " + this.mBody.getLinearVelocity().x + " Velocity Y: " + this.mBody.getLinearVelocity().y );
        System.out.println( "Position X: " + this.mBody.getPosition().x + " Position Y: " + this.mBody.getPosition().y );

        if( !this.mlastV && inputSystem.getV() ) {
            this.mModifier *= -1;
        }
        this.mlastV = inputSystem.getV();
        if( inputSystem.getA() ) {
            this.pt.setColor( Math.max( Math.min( this.pt.getColor().r + ( 0.1f * this.mModifier ), 1.0f ), 0.0f ),
                              Math.max( Math.min( this.pt.getColor().b + ( 0.1f * this.mModifier ), 1.0f ), 0.0f ),
                              Math.max( Math.min( this.pt.getColor().g + ( 0.1f * this.mModifier ), 1.0f ), 0.0f ),
                              1.0f );
        }
        if( inputSystem.getR() ) {
            this.pt.setColor( Math.max( Math.min( this.pt.getColor().r + ( 0.1f * this.mModifier ), 1.0f ), 0.0f ),
                              this.pt.getColor().g,
                              this.pt.getColor().b,
                              1.0f );
        }
        if( inputSystem.getG() ) {
            this.pt.setColor( this.pt.getColor().r,
                              Math.max( Math.min( this.pt.getColor().g + ( 0.1f * this.mModifier ), 1.0f ), 0.0f ),
                              this.pt.getColor().b,
                              1.0f );
        }
        if( inputSystem.getB() ) {
            this.pt.setColor( this.pt.getColor().r,
                              this.pt.getColor().g,
                              Math.max( Math.min( this.pt.getColor().b + ( 0.1f * this.mModifier ), 1.0f ), 0.0f ),
                              1.0f );
        }

        renderSystem.swap( mRenderer, mCamera );
        mRenderer.render();
        //Matrix4 camCopy = this.mCamera.combined.cpy();
        this.mDebugRenderer.render( ObjectRegistry.box2DSystem.getWorld(), this.mCamera.combined );
        rayHandler.setCombinedMatrix( this.mCamera.combined );
        rayHandler.updateAndRender();
    }

    @Override
    public void resize( int width, int height ) {
        this.mViewport.update( width, height );
        this.mCamera.position.set( this.mCamera.viewportWidth / 2, this.mCamera.viewportHeight / 2, 0 );
   }
}
