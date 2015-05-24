package nickhartung.learninggdx.physics;

import nickhartung.learninggdx.GameComponent;
import nickhartung.learninggdx.GameObject;

/**
 * Created by guntrah on 5/24/2015.
 */
public abstract class CollisionComponent extends GameComponent {

    public enum HitType {
        NONE,
        DAMAGE,
        COLLECT,
    }

    private boolean mAttackObject;
    private boolean mVulnerableObject;
    private HitType mHitType;

    public CollisionComponent() {
        super();
        this.reset();
    }

    public void setAttackObject( final boolean pAttack ) {
        this.mAttackObject = pAttack;
    }

    public void setVulnerableObject( final boolean pVulnerable ) {
        this.mVulnerableObject = pVulnerable;
    }

    public void setHitType( final HitType pHitType ) {
        this.mHitType = pHitType;
    }

    public boolean isAttackObject() {
        return this.mAttackObject;
    }

    public boolean isVulnerableObject() {
        return this.mVulnerableObject;
    }

    public HitType getHitType() {
        return this.mHitType;
    }

    public abstract boolean receivedHit( final GameObject pThisObject, final GameObject pOtherObject, final HitType pHitType );
    public abstract void    hitVictim(   final GameObject pThisObject, final GameObject pOtherObject, final HitType pHitType, final boolean pHitAccepted );

    @Override
    public void reset() {
        this.mHitType          = HitType.NONE;
        this.mAttackObject     = false;
        this.mVulnerableObject = false;
    }

}
