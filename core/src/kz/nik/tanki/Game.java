package kz.nik.tanki;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import kz.nik.tanki.bullets.Bullet;
import kz.nik.tanki.emitters.BotEmitter;
import kz.nik.tanki.emitters.BulletEmitter;
import kz.nik.tanki.map.Map;
import kz.nik.tanki.units.BotTank;
import kz.nik.tanki.units.PlayerTank;
import kz.nik.tanki.units.Tank;

public class Game extends com.badlogic.gdx.Game {
    private SpriteBatch batch;




    @Override
    public void create() {
        batch=new SpriteBatch();
        ScreenManager.getInstance().init(this,batch);
        ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }


    @Override
    public void dispose() {
        batch.dispose();

    }
}
