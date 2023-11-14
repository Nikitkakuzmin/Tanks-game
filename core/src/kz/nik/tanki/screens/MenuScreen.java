package kz.nik.tanki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import kz.nik.tanki.ScreenManager;
import kz.nik.tanki.bullets.Bullet;
import kz.nik.tanki.emitters.BotEmitter;
import kz.nik.tanki.emitters.BulletEmitter;
import kz.nik.tanki.map.Map;
import kz.nik.tanki.units.BotTank;
import kz.nik.tanki.units.PlayerTank;
import kz.nik.tanki.units.Tank;
import kz.nik.tanki.utils.GameType;
import kz.nik.tanki.utils.KeysControl;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends AbstractScreen {
    private SpriteBatch batch;
    private BitmapFont font24;
    private Stage stage;
    private TextureAtlas atlas;




    public MenuScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("game.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));

        stage = new Stage();



        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("SimpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        Group group = new Group();
        final TextButton startButton1 = new TextButton("Start 1 player", textButtonStyle);
        final TextButton startButton2 = new TextButton("Start 2 players", textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);

        startButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME, GameType.ONE_PLAYER);
            }
        });
        startButton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME,GameType.TWO_PLAYERS);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        startButton1.setPosition(0, 80);
        startButton2.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(startButton1);
        group.addActor(startButton2);
        group.addActor(exitButton);
        group.setPosition(580, 300);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 0.5f, 0, 1);
        stage.draw();

    }



    public void update(float dt) {
        stage.act(dt);
    }


    @Override
    public void dispose() {
        atlas.dispose();
        font24.dispose();
        stage.dispose();
    }




}
