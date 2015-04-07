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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import nickhartung.learninggdx.physics.box2d.Box2DMovementComponent;
import nickhartung.learninggdx.physics.box2d.Box2DWorldComponent;
import nickhartung.learninggdx.physics.standalone.MovementComponent;
import nickhartung.libgdx.render.BoxShapeDrawable;
import nickhartung.libgdx.render.CircleShapeDrawable;
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

    private GameObject test;
    private GameObject test2;
    private GameObject test3;
    private GameObject test4;
    private ObjectManager manager;

    private float      mModifier;
    private boolean    mlastV;
    private Light pt;

    private Body testBod;

    private Box2DDebugRenderer mDebugRenderer;

    @Override
    public void create() {
        this.mCamera = new OrthographicCamera();
        this.mViewport = new FitViewport( 960.0f, 540.0f, this.mCamera );
        this.mViewport.apply();

        this.mCamera.position.set( this.mCamera.viewportWidth / 2, this.mCamera.viewportHeight / 2, 0 );

        this.mSprite = new Sprite( new Texture( "badlogic.jpg" ) );
        this.mSprite.setPosition( 0.0f, 0.0f );
        this.mSprite.setSize( 100.0f, 100.0f );

        this.mRenderer = new Renderer();
        //this.mSpriteBatch = new SpriteBatch( 1000, createDefaultShader() );
        this.mSpriteBatch = new SpriteBatch();
        this.mShapeRenderer = new ShapeRenderer();

        RenderSystem render = new RenderSystem();
        render.setRenderers( this.mSpriteBatch, this.mShapeRenderer );
        ObjectRegistry.renderSystem = render;

        World world = new World( new Vector2( 0, 0 ), false );
        mDebugRenderer = new Box2DDebugRenderer();
        world.setContactListener( new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

        });

        ObjectRegistry.box2Dworld = world;
        RayHandler rayHandler = new RayHandler( world );
        rayHandler.setCulling( true );
        rayHandler.setShadows( true );
        rayHandler.useDiffuseLight( true );
        rayHandler.setAmbientLight( 1.0f, 1.0f, 1.0f, 1.0f );
        rayHandler.setBlur( false );
        //rayHandler.setBlur( true );
        //rayHandler.setBlurNum( 50 );
        ObjectRegistry.rayHandler = rayHandler;

        // Box2d ~~~~~~~~~~~~~~~~~~~~~~
        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.DynamicBody;
        groundBodyDef.position.set( 400.0f, 400.0f );

        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox( 50.0f, 50.0f );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0.6f;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = true;

        LightBodyComponent lightBody = new LightBodyComponent();
        lightBody.createBody( groundBodyDef, fixtureDef );
        lightBody.setOffset( 50.0f, 50.0f );

        LightBodyComponent lightBody2 = new LightBodyComponent();
        lightBody2.createBody( groundBodyDef, fixtureDef );
        lightBody2.setOffset( 50.0f, 50.0f );

        this.testBod = world.createBody( groundBodyDef );
        this.testBod.createFixture( fixtureDef );

        // Clean up after ourselves
        groundBox.dispose();

        BodyDef circleBodyDef = new BodyDef();
        circleBodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius( 75.0f );

        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = circleShape;
        fixtureDef2.density = 1.0f;
        fixtureDef2.isSensor = true;

        LightBodyComponent lightBody3 = new LightBodyComponent();
        lightBody3.createBody( circleBodyDef, fixtureDef2 );

        ////////////////////////////////////

        InputSystem inputSystem = new InputSystem();
        ObjectRegistry.inputSystem = inputSystem;

        Gdx.input.setInputProcessor( new DebugInputProcessor() );

        ///////////////////
        Sprite sprite1 = new Sprite( new Texture( "badlogic.jpg" ) );
        sprite1.setSize( 100.0f, 100.0f );
        RenderComponent renderCom = new RenderComponent();
        SpriteDrawable spriteDrawable = new SpriteDrawable();
        spriteDrawable.setSprite( sprite1 );
        renderCom.setDrawable( spriteDrawable );
        renderCom.setPriority( 2 );

        Sprite backgroundSprite = new Sprite( new Texture( "background.jpg" ) );
        backgroundSprite.setSize( 960.0f, 540.0f );
        backgroundSprite.setColor( 0.0f, 0.0f, 1.0f, 1.0f );
        //backgroundSprite.setColor( 0.0f, 0.0f, 0.0f, 1.0f );
        RenderComponent renderBackground = new RenderComponent();
        SpriteDrawable backgroundDrawable = new SpriteDrawable();
        backgroundDrawable.setSprite( backgroundSprite );
        renderBackground.setDrawable( backgroundDrawable );
        renderBackground.setPriority( 0 );

        Sprite playerSprite = new Sprite( new Texture( "black.jpg") );
        playerSprite.setSize( 200.0f, 200.0f );
        playerSprite.setColor( 1.0f, 1.0f, 1.0f, 1.0f );
        SpriteDrawable playerDrawable = new SpriteDrawable();
        playerDrawable.setSprite( playerSprite );
        RenderComponent renderPlayer = new RenderComponent();
        renderPlayer.setDrawable( playerDrawable );
        renderPlayer.setPriority( 1 );

        Color red = new Color();
        red.set( 1.0f, 0.0f, 0.0f, 1.0f );

        Color blue = new Color();
        blue.set( 0.0f, 0.0f, 1.0f, 1.0f );

        BoxShapeDrawable box = new BoxShapeDrawable();
        box.set( 0.0f, 100.0f, 100.0f, 0.0f );
        box.setColor( red );
        box.setShapeType( ShapeRenderer.ShapeType.Line );
        Vector2 pos = new Vector2();
        pos.set( 150.0f, 150.0f );

        CircleShapeDrawable circle = new CircleShapeDrawable();
        circle.set( 75.0f, 50 );
        circle.setColor( blue );
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
        testComp.setMovementSpeed( 300.0f, 300.0f * 60.0f );

        DumbMovementComponent dumbMove = new DumbMovementComponent();
        dumbMove.setMovementSpeed( 100.0f, 100.0f * 60.0f );

        DumbMovementComponent dumbMove2 = new DumbMovementComponent();
        dumbMove2.setMovementSpeed( 100.0f, 100.0f );

        DumbMovementComponent dumbMove3 = new DumbMovementComponent();
        dumbMove3.setMovementSpeed( 100.0f, 100.0f );

        this.pt = new PointLight( rayHandler, 512, new Color( 0.0f, 0.0f, 0.0f, 1.0f ), 500.0f, 0.0f, 0.0f );
        this.pt.setSoft( true );
        this.pt.setSoftnessLength( 100.0f );
        //this.pt = new ConeLight( rayHandler, 128, new Color( 0.0f, 0.0f, 0.0f, 1.0f ), 500.0f, 0.0f, 0.0f, 0.0f, 180.0f );
        //this.pt.setXray( true );
        this.mModifier = 1.0f;
        //pt.setStaticLight(true);

        LightComponent lightCom = new LightComponent();
        lightCom.setLight( pt );
        lightCom.setOffset( 100.0f, 100.0f );

        Sprite one = new Sprite( new Texture( "one.jpg" ) );
        one.setSize( 100.0f, 100.0f );

        SpriteDrawable oneDrawable = new SpriteDrawable();
        oneDrawable.setSprite( one );

        Sprite two = new Sprite( new Texture( "two.jpg" ) );
        two.setSize( 100.0f, 100.0f );

        SpriteDrawable twoDrawable = new SpriteDrawable();
        twoDrawable.setSprite( two );

        Sprite three = new Sprite( new Texture( "three.jpg" ) );
        three.setSize( 100.0f, 100.0f );

        SpriteDrawable threeDrawable = new SpriteDrawable();
        threeDrawable.setSprite( three );

        Sprite four = new Sprite( new Texture( "four.jpg" ) );
        four.setSize( 100.0f, 100.0f );

        SpriteDrawable fourDrawable = new SpriteDrawable();
        fourDrawable.setSprite( four );

        Sprite five = new Sprite( new Texture( "five.jpg" ) );
        five.setSize( 100.0f, 100.0f );

        SpriteDrawable fiveDrawable = new SpriteDrawable();
        fiveDrawable.setSprite( five );

        AnimationFrame idleFrame   = new AnimationFrame( spriteDrawable,   0.5f );

        AnimationFrame oneFrame   = new AnimationFrame( oneDrawable,   0.5f );
        AnimationFrame twoFrame   = new AnimationFrame( twoDrawable,   0.5f );
        AnimationFrame threeFrame = new AnimationFrame( threeDrawable, 0.5f );
        AnimationFrame fourFrame  = new AnimationFrame( fourDrawable,  0.5f );
        AnimationFrame fiveFrame  = new AnimationFrame( fiveDrawable,  0.5f );

        Animation animationMove = new Animation( GameObject.ActionType.MOVE.ordinal(), 5 );
        animationMove.setLoop( true );
        animationMove.addFrame( oneFrame );
        animationMove.addFrame( twoFrame );
        animationMove.addFrame( threeFrame );
        animationMove.addFrame( fourFrame );
        animationMove.addFrame( fiveFrame );

        Animation animationIdle = new Animation( GameObject.ActionType.IDLE.ordinal(), 1 );
        animationIdle.setLoop( false );
        animationIdle.addFrame( idleFrame );

        RenderComponent animRenderer = new RenderComponent();
        animRenderer.setPriority( 0 );

        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.addAnimation( animationIdle );
        animationComponent.addAnimation( animationMove );
        animationComponent.setRenderComponent( animRenderer );

        TestAnimationComponent testAnimation = new TestAnimationComponent();
        testAnimation.setAnimation( animationComponent );

        GameObject background = new GameObject();
        background.setCurrentAction( GameObject.ActionType.IDLE );
        background.add( renderBackground );
        background.setPosition( new Vector2( 0.0f, 0.0f ) );

        test = new GameObject();
        test.setCurrentAction( GameObject.ActionType.IDLE );
        //test.add( renderPlayer );
        test.add( movement );
        test.add( testComp );
        test.add( lightCom );
        //test.add( animRenderer );
        //test.add( animationComponent );
        //test.add( testAnimation );
        test.setPosition( test.getPosition().add( 0.0f, 0.0f ) );

        test2 = new GameObject();
        test2.add( renderCom );
        test2.add( lightBody );
        test2.setPosition( test2.getPosition().add( 100.0f, 100.0f ) );

        test3 = new GameObject();
        test3.add( boxRenderer );
        test3.add( lightBody2 );
        test3.setPosition( test3.getPosition().add( 150.0f, 150.0f ) );

        test4 = new GameObject();
        test4.add( circleRenderer );
        test4.add( lightBody3 );
        test4.add( movement );
        test4.add( dumbMove );
        test4.setPosition( test4.getPosition().add( 100.0f, 100.0f ) );

        Box2DMovementComponent boxMove = new Box2DMovementComponent();
        boxMove.setBody( this.testBod );
        GameObject test5 = new GameObject();
        test5.add( dumbMove2 );
        test5.add( boxMove );
        test5.add( boxRenderer );

        MovementComponent movementComponent = new MovementComponent();
        GameObject test6 = new GameObject();
        test6.add( dumbMove3 );
        test6.add( movementComponent );
        test6.add( boxRenderer );
        test6.setPosition( test6.getPosition().add( 400.0f, 300.0f ) );

        GameObject box2DObject = new GameObject();
        Box2DWorldComponent worldCom = new Box2DWorldComponent();
        worldCom.setWorld( world );

        manager = new ObjectManager( 64 );
        manager.add( background );
        //manager.add( test );
        //manager.add( test2 );
        //manager.add( test3 );
        //manager.add( test4 );
        manager.add( worldCom );
        manager.add( test5 );
        manager.add( test6 );
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
        //manager.update( delta, null );
        //ObjectRegistry.box2Dworld.step( delta, 6, 2 );
        manager.update( 60.0f / 60.0f, null );
        //ObjectRegistry.box2Dworld.step( 60.0f / 60.0f, 6, 2 );

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
        this.mDebugRenderer.render( ObjectRegistry.box2Dworld, this.mCamera.combined );
        rayHandler.setCombinedMatrix( this.mCamera.combined );
        rayHandler.updateAndRender();
    }

    @Override
    public void resize( int width, int height ) {
        this.mViewport.update( width, height );
        this.mCamera.position.set( this.mCamera.viewportWidth / 2, this.mCamera.viewportHeight / 2, 0 );
    }
}
