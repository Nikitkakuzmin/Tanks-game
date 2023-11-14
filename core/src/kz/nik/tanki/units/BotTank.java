package kz.nik.tanki.units;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import kz.nik.tanki.Game;
import kz.nik.tanki.screens.GameScreen;
import kz.nik.tanki.utils.Direction;
import kz.nik.tanki.utils.TankOwner;
import kz.nik.tanki.weapon.Weapon;

public class BotTank extends Tank {

    Direction preferredDirection;
    float aiTimer;
    float aiTimerTo;
    boolean active;
    float pursuitRadius;
    Vector3 lastPosition;

    public boolean isActive() {
        return active;
    }

    public BotTank(GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen);
        this.ownerType = TankOwner.AI;
        this.weapon = new Weapon(atlas);
        this.texture = atlas.findRegion("tank5enemy");
        this.textureHp = atlas.findRegion("bar");
        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100.0f;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.lastPosition = new Vector3(0.0f, 0.0f, 0.0f);
        this.pursuitRadius = 300.0f;
        this.hpMax = 5;
        this.hp = this.hpMax;
        this.aiTimerTo = 3.0f;
        this.preferredDirection = Direction.UP;
        this.circle = new Circle(position.x, position.y, (width + height) / 2);
    }

    public void activate(float x, float y) {
        hpMax = 5;
        hp = hpMax;
        preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        angle = preferredDirection.getAngle();
        position.set(x, y);
        aiTimer = 0.0f;
        active = true;
    }

    @Override
    public void destroy() {
        gameScreen.getItemsEmitter().generateRandomItem(position.x, position.y,3,0.5f);
        active = false;
    }

    public void update(float dt) {
        aiTimer += dt;

        if (aiTimer >= aiTimerTo) {
            aiTimer = 0.0f;
            aiTimerTo = MathUtils.random(3.5f, 6.0f);
            preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
            angle = preferredDirection.getAngle();
        }
        move(preferredDirection, dt);

        PlayerTank preferredTarget=null;
        if(gameScreen.getPlayers().size()==1){
            preferredTarget = gameScreen.getPlayers().get(0);
        }else {
            float minDist=Float.MAX_VALUE;
            for (int i = 0; i < gameScreen.getPlayers().size(); i++) {
                PlayerTank player = gameScreen.getPlayers().get(i);
                float dst = this.position.dst(player.getPosition());
                if(dst < minDist){
                    minDist = dst;
                    preferredTarget=player;
                }
            }
        }

        float dst = this.position.dst(preferredTarget.getPosition());
        if (dst < pursuitRadius) {
            rotateTurretToPoint(preferredTarget.getPosition().x,preferredTarget.getPosition().y, dt);
            fire();
        }

        if (Math.abs(position.x - lastPosition.x) < 0.5f && Math.abs(position.y - lastPosition.y) < 0.5f) {
            lastPosition.z += dt;
            if (lastPosition.z > 0.3f) {
                aiTimer += 10.0f;
            }
        }else{
            lastPosition.x=position.x;
            lastPosition.y=position.y;
            lastPosition.z=0.0f;
        }
        super.update(dt);
    }
}
