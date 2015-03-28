package nickhartung.learninggdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Nick on 3/24/2015.
 */
public class DebugInputProcessor implements InputProcessor {
    @Override
    public boolean keyDown( int keycode ) {
        InputSystem input = ObjectRegistry.inputSystem;
        switch( keycode ) {
            case Input.Keys.DOWN:
                input.setMoveDown( true );
                break;
            case Input.Keys.UP:
                input.setMoveUp( true );
                break;
            case Input.Keys.LEFT:
                input.setMoveLeft( true );
                break;
            case Input.Keys.RIGHT:
                input.setMoveRight( true );
                break;
            case Input.Keys.A:
                input.setA( true );
                break;
            case Input.Keys.R:
                input.setR( true );
                break;
            case Input.Keys.G:
                input.setG( true );
                break;
            case Input.Keys.B:
                input.setB( true );
                break;
            case Input.Keys.V:
                input.setV( true );
                break;
            default:
                // Do nothing
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp( int keycode ) {
        InputSystem input = ObjectRegistry.inputSystem;
        switch( keycode ) {
            case Input.Keys.DOWN:
                input.setMoveDown( false );
                break;
            case Input.Keys.UP:
                input.setMoveUp( false );
                break;
            case Input.Keys.LEFT:
                input.setMoveLeft( false );
                break;
            case Input.Keys.RIGHT:
                input.setMoveRight( false );
                break;
            case Input.Keys.A:
                input.setA( false );
                break;
            case Input.Keys.R:
                input.setR( false );
                break;
            case Input.Keys.G:
                input.setG( false );
                break;
            case Input.Keys.B:
                input.setB( false );
                break;
            case Input.Keys.V:
                input.setV( false );
                break;
            default:
                // Do nothing
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped( char character ) {
        return false;
    }

    @Override
    public boolean touchDown( int screenX, int screenY, int pointer, int button ) {
        return false;
    }

    @Override
    public boolean touchUp( int screenX, int screenY, int pointer, int button ) {
        return false;
    }

    @Override
    public boolean touchDragged( int screenX, int screenY, int pointer ) {
        return false;
    }

    @Override
    public boolean mouseMoved( int screenX, int screenY ) {
        return false;
    }

    @Override
    public boolean scrolled( int amount ) {
        return false;
    }
}
