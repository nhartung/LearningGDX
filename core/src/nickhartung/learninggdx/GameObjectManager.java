package nickhartung.learninggdx;

import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.libgdx.utilities.ObjectManager;

/**
 * Created by guntrah on 5/3/2015.
 */
public class GameObjectManager extends ObjectManager {

    public GameObjectManager() {
        super();
    }

    public GameObjectManager( final int arraySize ) {
        super( arraySize );
    }

    private int mStartPhase;
    private int mEndPhase;

    @Override
    public void add( final BaseObject object ) {
        if( object instanceof GameObject ) {
            super.add( object );
        } else {
            assert false : "Can't add a non-GameObject to a GameObjectManager!";
        }
    }

    @Override
    public void commitUpdates() {
        super.commitUpdates();
        final int count = this.getObjects().getCount();
        final Object[] objects = this.getObjects().getArray();
        for( int i = 0; i < count; i++ ) {
            final GameObject gameObject = (GameObject)objects[ i ];
            gameObject.setUpdatePhases( this.mStartPhase, this.mEndPhase );
        }
    }

    public void setUpdatePhases( final int pStartPhase, final int pEndPhase ) {
        this.mStartPhase = pStartPhase;
        this.mEndPhase   = pEndPhase;
    }
}
