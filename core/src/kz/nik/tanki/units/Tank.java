package kz.nik.tanki.units;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import kz.nik.tanki.Game;
import kz.nik.tanki.screens.GameScreen;
import kz.nik.tanki.utils.Direction;
import kz.nik.tanki.utils.TankOwner;
import kz.nik.tanki.utils.Utils;
import kz.nik.tanki.weapon.Weapon;


public abstract class Tank {

    GameScreen gameScreen;
    int hp;
    int hpMax;
    Weapon weapon;
    TextureRegion texture;
    TextureRegion textureHp;
    Vector2 position;
    Vector2 temp;
    TankOwner ownerType;
    float speed;
    float angle;
    float towerAngle;
    float fireTimer;
    Circle circle;

    int width;
    int height;

    public Tank(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.temp=new Vector2(0.0f,0.0f);
        /*this.weapon = new Weapon();
        this.texture = new Texture("Tank5.png");

        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100.0f;
        this.width = texture.getWidth();
        this.height = texture.getHeight();*/
    }

    public Vector2 getPosition() {
        return position;
    }

    public TankOwner getOwnerType() {
        return ownerType;
    }

    public Circle getCircle() {
        return circle;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2,
                width, height, 1, 1, angle);
        batch.draw(weapon.getTexture(), position.x - width / 2, position.y - height / 2, width / 2,
                height / 2, width, height, 1, 1, towerAngle);
        if (hp < hpMax) {
            batch.setColor(0, 0, 0, 1);
            batch.draw(textureHp, position.x - width / 2-2, position.y + height / 2-8-2, 44, 12);
            batch.setColor(1, 0, 0, 1);
            batch.draw(textureHp, position.x - width / 2, position.y + height / 2-8, ((float) hp / hpMax * 40), 8);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void takeDamage(int damage){
        hp-=damage;
        if(hp <= 0){
            destroy();
        }
    }

    public abstract void destroy();

    public void update(float dt) {
        fireTimer += dt;

        if (position.x < 0.0f) {
            position.x = 0.0f;
        }
        if (position.x > Gdx.graphics.getWidth()) {
            position.x = Gdx.graphics.getWidth();
        }
        if (position.y < 0.0f) {
            position.y = 0.0f;
        }
        if (position.y > Gdx.graphics.getHeight()) {
            position.y = Gdx.graphics.getHeight();
        }

        circle.setPosition(position);
    }

    public void rotateTurretToPoint(float pointX, float pointY, float dt) {
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        towerAngle = Utils.makeRotation(towerAngle, angleTo, 180.0f, dt);
        towerAngle = Utils.angleToFromNegPiToPosPi(towerAngle);

    }

    public void move(Direction direction,float dt){
        temp.set(position);
        temp.add(speed*direction.getVx()*dt,speed*direction.getVy()*dt);
        if(gameScreen.getMap().isAreaClear(temp.x,temp.y,width/2)){
            angle=direction.getAngle();
            position.set(temp);
        }

    }


    public void fire() {

        if (fireTimer >= weapon.getFirePeriod()) {
            fireTimer = 0.0f;

            float angleRad = (float) Math.toRadians(towerAngle);
            gameScreen.getBulletEmitter().activate(this,position.x, position.y, weapon.getProjectileSpeed() * (float) Math.cos(angleRad)
                    , weapon.getProjectileSpeed() * (float) Math.sin(angleRad), weapon.getDamage(),weapon.getProjectileLifeTime());
        }
        gameScreen.getTankShotSound().play();
    }
}
