package nickhartung.learninggdx;

import com.badlogic.gdx.Application;
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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.viewport.FitViewport;

import box2dLight.Light;
import box2dLight.RayHandler;
import nickhartung.learninggdx.physics.CollisionComponent;
import nickhartung.learninggdx.physics.TestCollisionComponent;
import nickhartung.learninggdx.physics.box2d.Box2DMovementComponent;
import nickhartung.learninggdx.physics.box2d.Box2DPositionUpdateComponent;
import nickhartung.learninggdx.physics.box2d.Box2DSystem;
import nickhartung.learninggdx.physics.standalone.MovementComponent;
import nickhartung.libgdx.render.BoxShapeDrawable;
import nickhartung.libgdx.render.RenderSystem;
import nickhartung.libgdx.render.Renderer;
import nickhartung.libgdx.render.SpriteDrawable;

public class GDXTop extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    private OrthographicCamera mCamera;
    private FitViewport mViewport;
    private Sprite mSprite;
    private Renderer mRenderer;
    private SpriteBatch mSpriteBatch;
    private ShapeRenderer mShapeRenderer;

    private GameObjectManager mManager;

    private float      mModifier;
    private boolean    mlastV;
    private Body       mBody;
    private GameObject test;
    private Light pt;

    private Body testBod;

    private Box2DDebugRenderer mDebugRenderer;

    public static float PIXELS_PER_METER = 100.0f;

    private float       mAccumulator;
    private boolean     mRenderReady;
    public static float UPDATE_RATE = 1.0f / 60.0f;

    @Override
    public void create() {
        Gdx.app.setLogLevel( Application.LOG_DEBUG );
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

        ContactListener contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                final GameObject objectA  = (GameObject)fixtureA.getBody().getUserData();
                final GameObject objectB  = (GameObject)fixtureB.getBody().getUserData();
                final CollisionComponent collisionA = (CollisionComponent)fixtureA.getUserData();
                final CollisionComponent collisionB = (CollisionComponent)fixtureB.getUserData();
                if( collisionA != null && collisionB != null ) {
                    if( collisionA.isAttackObject() && collisionB.isVulnerableObject() ) {
                        final boolean hitAccepted = collisionB.receivedHit( objectB, objectA, collisionA.getHitType() );
                        if( hitAccepted ) {
                            collisionA.hitVictim( objectA, objectB, collisionA.getHitType(), hitAccepted );
                        }
                    }
                    if( collisionB.isAttackObject() && collisionA.isVulnerableObject() ) {
                        final boolean hitAccepted = collisionA.receivedHit( objectA, objectB, collisionB.getHitType() );
                        if( hitAccepted ) {
                            collisionB.hitVictim( objectB, objectA, collisionB.getHitType(), hitAccepted );
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        };
        Box2DSystem box2DSystem = new Box2DSystem( new Vector2( 0, 0 ), false, 6, 2, 100, contactListener );
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

        this.mAccumulator = 0.0f;
        this.mRenderReady = false;
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

        GameObject box2DObject = new GameObject();
        TestCollisionComponent collisionComponent1 = new TestCollisionComponent();
        collisionComponent1.setAttackObject( true );
        collisionComponent1.setVulnerableObject( false );
        collisionComponent1.setHitType( CollisionComponent.HitType.DAMAGE );
        this.mBody = box2DSystem.createBody( groundBodyDef, fixtureDef, collisionComponent1, box2DObject );

        groundBodyDef.position.set( 300.0f / PIXELS_PER_METER, 100.0f / PIXELS_PER_METER );
        GameObject otherBox2DObject = new GameObject();
        TestCollisionComponent collisionComponent2 = new TestCollisionComponent();
        collisionComponent2.setAttackObject( false );
        collisionComponent2.setVulnerableObject( true );
        collisionComponent2.setHitType( CollisionComponent.HitType.DAMAGE );
        final Body otherBody = box2DSystem.createBody( groundBodyDef, fixtureDef, collisionComponent2, otherBox2DObject );

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

        RenderComponent box2DRenderComponent = new RenderComponent();
        box2DRenderComponent.setDrawable( boxDrawable );
        DumbMovementComponent dumbMoverBox2D = new DumbMovementComponent();
        dumbMoverBox2D.setMovementSpeed( 100.0f / PIXELS_PER_METER, 100.0f / PIXELS_PER_METER );
        Box2DMovementComponent box2DMover = new Box2DMovementComponent();
        box2DMover.setBody( this.mBody );

        Box2DPositionUpdateComponent box2DPosUpdater = new Box2DPositionUpdateComponent();
        box2DPosUpdater.setBody( this.mBody );

        BoxShapeDrawable boxDrawable2 = new BoxShapeDrawable();
        boxDrawable2.set( 0.0f, 100.0f, 100.0f, 0.0f );
        boxDrawable2.setColor( Color.BLUE );
        RenderComponent box2DRenderComponent2 = new RenderComponent();
        box2DRenderComponent2.setDrawable( boxDrawable2 );

        Box2DMovementComponent box2DMover2 = new Box2DMovementComponent();
        box2DMover2.setBody( otherBody );

        Box2DPositionUpdateComponent box2DPosUpdater2 = new Box2DPositionUpdateComponent();
        box2DPosUpdater2.setBody( otherBody );

        box2DObject.add( box2DRenderComponent );
        box2DObject.add( dumbMoverBox2D );
        box2DObject.add( box2DMover );
        box2DObject.add( box2DPosUpdater );
        box2DObject.getPosition().set( 0.0f, 100.0f );

        otherBox2DObject.add( box2DRenderComponent2 );
        otherBox2DObject.add( box2DMover2 );
        otherBox2DObject.add( box2DPosUpdater2 );
        otherBox2DObject.getPosition().set( 300.0f, 100.0f );

        this.test = box2DObject;
        this.mManager = new GameObjectManager( 64 );
        this.mManager.add( backgroundObject );
        this.mManager.add( standaloneObject );
        this.mManager.add( box2DObject );
        this.mManager.add( otherBox2DObject );
    }

    @Override
    public void render() {

        this.mCamera.update();

        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        final RenderSystem renderSystem = ObjectRegistry.renderSystem;
        //final RayHandler rayHandler = ObjectRegistry.rayHandler;
        final InputSystem inputSystem = ObjectRegistry.inputSystem;

        final float delta = Gdx.graphics.getDeltaTime();
        this.mAccumulator += delta;
        boolean updated = false;
        while( this.mAccumulator >= UPDATE_RATE ) {
            this.mManager.setUpdatePhases( GameComponent.ComponentPhases.THINK.ordinal(), GameComponent.ComponentPhases.POST_PHYSICS.ordinal() );
            this.mManager.update( UPDATE_RATE, null );
            ObjectRegistry.box2DSystem.update( UPDATE_RATE, null );
            this.mManager.setUpdatePhases( GameComponent.ComponentPhases.MOVEMENT.ordinal(), GameComponent.ComponentPhases.FRAME_END.ordinal() );
            this.mManager.update( UPDATE_RATE, null );
            this.mAccumulator -= UPDATE_RATE;
            updated = true;
        }

        // TODO: We need to do something with the leftover time in the accumulator here...

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

        if( updated ) {
            renderSystem.swap( mRenderer, mCamera );
            this.mRenderReady = true;
        }
        if( this.mRenderReady ) {
            mRenderer.render();
        }
        //Matrix4 camCopy = this.mCamera.combined.cpy();
        this.mDebugRenderer.render( ObjectRegistry.box2DSystem.getWorld(), this.mCamera.combined );
        //rayHandler.setCombinedMatrix( this.mCamera.combined );
        //rayHandler.updateAndRender();
    }

    @Override
    public void dispose() {
        ObjectRegistry.box2DSystem.destroyBody( this.test );
    }

    @Override
    public void resize( int width, int height ) {
        this.mViewport.update( width, height );
        this.mCamera.position.set( this.mCamera.viewportWidth / 2, this.mCamera.viewportHeight / 2, 0 );
   }
}
