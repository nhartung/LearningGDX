package nickhartung.learninggdx.physics;

import com.badlogic.gdx.Gdx;

import nickhartung.learninggdx.GameObject;

/**
 * Created by guntrah on 5/24/2015.
 */
public class TestCollisionComponent extends CollisionComponent {

    @Override
    public boolean receivedHit( final GameObject pThisObject, final GameObject pOtherObject, final HitType pHitType ) {
        boolean ret = false;
        switch( pHitType ) {
            case DAMAGE:
                Gdx.app.debug( "Received Hit", pThisObject.toString() + " damaged by " + pOtherObject.toString() );
                ret = true;
                break;
            case COLLECT:
                Gdx.app.debug( "Received Hit", pThisObject.toString() + " collected " + pOtherObject.toString() );
                ret = true;
                break;
        }
        return ret;
    }

    public void hitVictim( final GameObject pThisObject, final GameObject pOtherObject, final HitType pHitType, final boolean pHitAccepted ) {
        if( pHitAccepted ) {
            switch( pHitType ) {
                case DAMAGE:
                    Gdx.app.debug( "Hit Victim", pThisObject.toString() + " damaged " + pOtherObject.toString() );
                    break;
                case COLLECT:
                    Gdx.app.debug( "Hit Victim", pThisObject.toString() + " was collected by " + pOtherObject.toString() );
                    break;
            }
        }
    }
}
