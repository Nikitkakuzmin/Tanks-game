package kz.nik.tanki.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import kz.nik.tanki.Game;
import kz.nik.tanki.ScreenManager;
import kz.nik.tanki.powerUps.Item;
import kz.nik.tanki.screens.GameScreen;
import kz.nik.tanki.utils.Direction;
import kz.nik.tanki.utils.KeysControl;
import kz.nik.tanki.utils.TankOwner;
import kz.nik.tanki.utils.Utils;
import kz.nik.tanki.weapon.Weapon;

public class PlayerTank extends Tank {
    KeysControl keysControl;
    StringBuilder tempString;
    int lives;
    int index;
    int score;

    public PlayerTank(int index, GameScreen gameScreen, KeysControl keysControl, TextureAtlas atlas) {
        super(gameScreen);
        this.index = index;
        this.ownerType = TankOwner.PLAYER;
        this.keysControl = keysControl;
        this.weapon = new Weapon(atlas);
        this.texture = atlas.findRegion("tank5");
        this.textureHp = atlas.findRegion("bar");
        this.position = new Vector2(120.0f, 120.0f);
        this.speed = 100.0f;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 10;
        this.hp = hpMax;
        this.circle = new Circle(position.x, position.y, (width + height) / 2);
        this.lives=5;
        this.tempString=new StringBuilder();
    }

    public  void consumePowerUp(Item item){
        switch(item.getType()){
            case MEDKIT:
                hp+=4;
                if(hp>hpMax){
                    hp=hpMax;
                }
                break;
            case SHIELD:
                addScore(1000);
                break;
        }
    }

    public void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(keysControl.getLeft())) {
            move(Direction.LEFT, dt);
        } else if (Gdx.input.isKeyPressed(keysControl.getRight())) {
            move(Direction.RIGHT, dt);
        } else if (Gdx.input.isKeyPressed(keysControl.getUp())) {
            move(Direction.UP, dt);
        } else if (Gdx.input.isKeyPressed(keysControl.getDown())) {
            move(Direction.DOWN, dt);

        }

    }

    @Override
    public void destroy() {
        lives--;
        hp = hpMax;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24) {
        tempString.setLength(0);
        tempString.append("Player: " ).append(index);
        tempString.append("\nScore: " ).append(score);
        tempString.append("\nLives: " ).append(lives);
        font24.draw(batch, tempString, 10 + (index - 1) * 200, 700);
    }

    public void update(float dt) {
        /*x+=speed * dt;*/
        /*if(Gdx.input.isKeyPressed(Input.Keys.A)){
            x-=speed*dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            x+=speed*dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            y+=speed*dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            y-=speed*dt;
        }*/
        checkMovement(dt);
        /*float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        temp.set(Gdx.input.getX(),Gdx.input.getY());

        ScreenManager.getInstance().getViewport().unproject(temp);*/
        if (keysControl.getTargeting() == KeysControl.Targeting.MOUSE) {
            rotateTurretToPoint(gameScreen.getMousePosition().x, gameScreen.getMousePosition().y, dt);

            if (Gdx.input.isTouched()) {
                fire();
            }
        }else{
            if(Gdx.input.isKeyPressed(keysControl.getRotateTurretLeft())){
                towerAngle = Utils.makeRotation(towerAngle, towerAngle+90.0f, 180.0f, dt);
                towerAngle = Utils.angleToFromNegPiToPosPi(towerAngle);
            }
            if(Gdx.input.isKeyPressed(keysControl.getRotateTurretRight())){
                towerAngle = Utils.makeRotation(towerAngle, towerAngle-90.0f, 180.0f, dt);
                towerAngle = Utils.angleToFromNegPiToPosPi(towerAngle);
            }
            if(Gdx.input.isKeyPressed(keysControl.getFire())){
                fire();
            }
        }
        super.update(dt);

    }
}
