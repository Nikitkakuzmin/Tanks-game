package kz.nik.tanki.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private TextureRegion texture;
    private float firePeriod;
    private float radius;
    private int damage;
    private float projectileSpeed;
    private float projectileLifeTime;

    public TextureRegion getTexture() {
        return texture;
    }

    public float getRadius() {
        return radius;
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public float getProjectileLifeTime() {
        return projectileLifeTime;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public Weapon(TextureAtlas atlas){
        this.texture = atlas.findRegion("tower2");
        this.firePeriod =0.4f;
        this.damage =1;
        this.radius =1000.0f;
        this.projectileSpeed=2000.0f;
        this.projectileLifeTime=this.radius/this.projectileSpeed;
    }
}
