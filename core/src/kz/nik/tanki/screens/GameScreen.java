package kz.nik.tanki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import kz.nik.tanki.powerUps.Item;
import kz.nik.tanki.powerUps.ItemsEmitter;
import kz.nik.tanki.units.BotTank;
import kz.nik.tanki.units.PlayerTank;
import kz.nik.tanki.units.Tank;
import kz.nik.tanki.utils.GameType;
import kz.nik.tanki.utils.KeysControl;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends AbstractScreen {
    private SpriteBatch batch;
    private BitmapFont font24;
    private List<PlayerTank> players;
    private TextureAtlas atlas;
    private Map map;
    private BulletEmitter bulletEmitter;
    private BotEmitter botEmitter;
    private ItemsEmitter itemsEmitter;
    private float gameTimer;
    private Stage stage;
    private Vector2 mousePosition;
    private boolean paused;
    private TextureRegion cursor;
    private float worldTimer;
    private GameType gameType;
     private Sound sound;
     private Music music;
    private static final boolean FRIENDLY_FIRE = false;

    private Sound tankShotSound;

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public List<PlayerTank> getPlayers() {
        return players;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public Map getMap() {
        return map;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        this.gameType = GameType.ONE_PLAYER;
    }

    public ItemsEmitter getItemsEmitter() {
        return itemsEmitter;
    }

    public Sound getTankShotSound() {
        return tankShotSound;
    }

    @Override
    public void show() {
        /*sound=Gdx.audio.newSound(Gdx.files.internal("sound"));
        sound.play();*/
        tankShotSound = Gdx.audio.newSound(Gdx.files.internal("assets/shot.mp3"));
        atlas = new TextureAtlas("game.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        cursor = new TextureRegion(atlas.findRegion("cursor"));
        map = new Map(atlas);

        players = new ArrayList<>();
        players.add(new PlayerTank(1, this, KeysControl.creteStandardControl1(), atlas));
        if (gameType == GameType.TWO_PLAYERS) {
            players.add(new PlayerTank(2, this, KeysControl.creteStandardControl2(), atlas));
        }
        bulletEmitter = new BulletEmitter(atlas);
        itemsEmitter = new ItemsEmitter(atlas);
        botEmitter = new BotEmitter(this, atlas);
        gameTimer = 100.0f;
        stage = new Stage();
        mousePosition = new Vector2();


        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("SimpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        Group group = new Group();
        final TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
            }
        });
        pauseButton.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(pauseButton);
        group.addActor(exitButton);
        group.setPosition(1000, 600);
        stage.addActor(group);
        /*itemsEmitter.generateRandomItem(300,300,5,0.8f);*/
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 0f, 0, 1);

        //следить за игроком
        /*ScreenManager.getInstance().getCamera().position.set(player.getPosition().x,player.getPosition().y,0);
        ScreenManager.getInstance().getCamera().update();*/

        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        map.render(batch);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).render(batch);
        }

        botEmitter.render(batch);
        bulletEmitter.render(batch);
        itemsEmitter.render(batch);


        for (int i = 0; i < players.size(); i++) {
            players.get(i).renderHUD(batch, font24);
        }

        batch.end();
        stage.draw();
        batch.begin();
        batch.draw(cursor, mousePosition.x - cursor.getRegionWidth() / 2, mousePosition.y - cursor.getRegionHeight() / 2,
                cursor.getRegionWidth() / 2, cursor.getRegionHeight() / 2,
                cursor.getRegionWidth(), cursor.getRegionHeight(), 1, 1, worldTimer * 15);

        batch.end();
    }


    @Override
    public void dispose() {
        atlas.dispose();
        font24.dispose();
        tankShotSound.dispose();
    }

    public void update(float dt) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);
        worldTimer += dt;
        if (!paused) {
            gameTimer += dt;
            if (gameTimer > 15.0f) {
                gameTimer = 0.0f;

                for (int i = 0; i < 5; i++) {
                    float coordX, coordY;
                    do {
                        coordX = MathUtils.random(0, Gdx.graphics.getWidth());
                        coordY = MathUtils.random(0, Gdx.graphics.getHeight());
                    } while (!map.isAreaClear(coordX, coordY, 25));

                    botEmitter.activate(coordX, coordY);
                }

              /*  float coordX, coordY;
                do {
                    coordX = MathUtils.random(0, Gdx.graphics.getWidth());
                    coordY = MathUtils.random(0, Gdx.graphics.getHeight());
                } while (!map.isAreaClear(coordX, coordY, 25));

                botEmitter.activate(coordX, coordY);*/
            }
            for (int i = 0; i < players.size(); i++) {
                players.get(i).update(dt);
            }
            botEmitter.update(dt);
            bulletEmitter.update(dt);
            itemsEmitter.update(dt);
            checkCollisions();
        }
        stage.act(dt);
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if (bullet.isActive()) {
                for (int j = 0; j < botEmitter.getBots().length; j++) {
                    BotTank bot = botEmitter.getBots()[j];
                    if (bot.isActive()) {
                        /*if(bullet.getOwner()!=bot && bot.getCircle().contains(bullet.getPosition())){*/
                        if (checkBulletAndTank(bot, bullet) && bot.getCircle().contains(bullet.getPosition())) {
                            bullet.deactivate();
                            bot.takeDamage(bullet.getDamage());
                            break;
                        }
                    }
                }
                for (int j = 0; j < players.size(); j++) {
                    PlayerTank player = players.get(j);
                    if (checkBulletAndTank(player, bullet) && player.getCircle().contains(bullet.getPosition())) {
                        bullet.deactivate();
                        player.takeDamage(bullet.getDamage());
                    }
                }
                map.checkWallAndBulletsCollision(bullet);
            }
        }
        for (int i = 0; i < itemsEmitter.getItems().length; i++) {
            if (itemsEmitter.getItems()[i].isActive()) {
                Item item = itemsEmitter.getItems()[i];
                for (int j = 0; j < players.size(); j++) {
                    if (players.get(j).getCircle().contains(item.getPosition())) {
                        players.get(j).consumePowerUp(item);
                        item.deactivate();
                        break;
                    }
                }
            }
        }
    }

    public boolean checkBulletAndTank(Tank tank, Bullet bullet) {
        if (!FRIENDLY_FIRE) {
            return tank.getOwnerType() != bullet.getOwner().getOwnerType();
        } else {
            return tank != bullet.getOwner();
        }
    }
}

