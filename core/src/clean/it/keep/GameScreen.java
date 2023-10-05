package clean.it.keep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
public class GameScreen implements Screen {

    final KeepItClean game;
    private OrthographicCamera camera;
    private Texture dropImage1;
    private Texture dropImage2;
    private Texture dropImage3;
    private Texture dropImage4;
    private Sound dropSound;
    private Music rainMusic;
    private Player player1;
    private Array<Rectangle> trashDrops;
    private Array<Rectangle> raindrops1;
    private Array<Rectangle> raindrops2;
    private Array<Rectangle> raindrops3;
    private Array<Rectangle> raindrops4;
    private Array<String> raindropPaths;
    private long lastDropTime;
    private int player1Score = 0;
    private int player1Speed = 600;
    private int spawnDiff = 500000000;
    private int dropSpeed = 200;
    private int dropVib = 10;
    private int dropleaks = 0;
    final Texture Background;
//    private String[] trashPaths = {"banana.png","battery.png","glass-bottle.png","plastic-bag.png"};

    public GameScreen(final KeepItClean game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        String[] paths = {"blueBin.png","redBin.png","greenBin.png","yellowBin.png"};
        player1 = new Player(704, 20, 64, 64, paths);

        dropImage1 = new Texture(Gdx.files.internal("banana.png"));
        dropImage2 = new Texture(Gdx.files.internal("battery.png"));
        dropImage3 = new Texture(Gdx.files.internal("plastic-bag.png"));
        dropImage4 = new Texture(Gdx.files.internal("glass-bottle.png"));

        raindrops1 = new Array<Rectangle>();
        raindrops2 = new Array<Rectangle>();
        raindrops3 = new Array<Rectangle>();
        raindrops4 = new Array<Rectangle>();
        spawnTrashDrop();

        dropSound = Gdx.audio.newSound(Gdx.files.internal("garbageSoundEffect.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("bgMusic.mp3"));

        Background = new Texture(Gdx.files.internal("background.jpg"));

        rainMusic.setLooping(true);
    }

private void spawnTrashDrop() {
    Rectangle raindrop1 = new Rectangle();
    raindrop1.x = MathUtils.random(0, 800 - 64);
    raindrop1.y = 480;
    raindrop1.width = 64;
    raindrop1.height = 64;
    raindrops1.add(raindrop1);
    lastDropTime = TimeUtils.nanoTime();

    Rectangle raindrop2 = new Rectangle();
    raindrop2.x = MathUtils.random(0, 800 - 64);
    raindrop2.y = 480;
    raindrop2.width = 64;
    raindrop2.height = 64;
    raindrops2.add(raindrop2);
    lastDropTime = TimeUtils.nanoTime();

    Rectangle raindrop3 = new Rectangle();
    raindrop3.x = MathUtils.random(0, 800 - 64);
    raindrop3.y = 480;
    raindrop3.width = 64;
    raindrop3.height = 64;
    raindrops3.add(raindrop3);
    lastDropTime = TimeUtils.nanoTime();

    Rectangle raindrop4 = new Rectangle();
    raindrop4.x = MathUtils.random(0, 800 - 64);
    raindrop4.y = 480;
    raindrop4.width = 64;
    raindrop4.height = 64;
    raindrops4.add(raindrop4);
    lastDropTime = TimeUtils.nanoTime();
}
    @Override
    public void render (float delta) {
        ScreenUtils.clear(0,0,0.4f,1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            player1.setTexture(1); // Change to the redBin.png texture (index 1)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player1.setTexture(0); // Change to the redBin.png texture (index 0)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            player1.setTexture(2); // Change to the redBin.png texture (index 2)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            player1.setTexture(3); // Change to the redBin.png texture (index 3)
        }


        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) )
            player1.getRectangle().x -= player1Speed * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) )
            player1.getRectangle().x += player1Speed * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.UP) )
            player1.getRectangle().y += player1Speed * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) )
            player1.getRectangle().y -= player1Speed * Gdx.graphics.getDeltaTime();

        if(player1.getRectangle().x < 0)
            player1.getRectangle().x = 0;
        if(player1.getRectangle().x > 800 - 64)
            player1.getRectangle().x = 800 - 64;
        if(player1.getRectangle().y < 0)
            player1.getRectangle().y = 0;
        if(player1.getRectangle().y > 480 - 64)
            player1.getRectangle().y = 480 -64;

        if(TimeUtils.nanoTime() - lastDropTime > spawnDiff)
            spawnTrashDrop();

        for (Iterator<Rectangle> iter1 = raindrops1.iterator(); iter1.hasNext(); ) {
            Rectangle raindrop1 = iter1.next();
            raindrop1.y -= dropSpeed * Gdx.graphics.getDeltaTime();
            raindrop1.x += MathUtils.random(-dropVib, dropVib) * Gdx.graphics.getDeltaTime();
            if (raindrop1.y + 64 < 0) {
                dropleaks++;
                iter1.remove();
            }
            // player --> speed up when gathering 5 drops
            // other player --> slower

            if (raindrop1.overlaps(player1.getRectangle())) {
                dropSound.play();
                player1Score++;
                if (player1Score % 5 == 0) {
                    player1Speed += 50;
                }
                iter1.remove();
            }

        }

        for (Iterator<Rectangle> iter2 = raindrops2.iterator(); iter2.hasNext(); ) {
            Rectangle raindrop2 = iter2.next();
            raindrop2.y -= dropSpeed * Gdx.graphics.getDeltaTime();
            raindrop2.x += MathUtils.random(-dropVib, dropVib) * Gdx.graphics.getDeltaTime();
            if (raindrop2.y + 64 < 0) {
                dropleaks++;
                iter2.remove();
            }
            if (raindrop2.overlaps(player1.getRectangle())) {
                dropSound.play();
                player1Score++;
                if (player1Score % 5 == 0) {
                    player1Speed += 50;
                }
                iter2.remove();

            }
        }

        for (Iterator<Rectangle> iter3 = raindrops3.iterator(); iter3.hasNext(); ) {
            Rectangle raindrop3 = iter3.next();
            raindrop3.y -= dropSpeed * Gdx.graphics.getDeltaTime();
            raindrop3.x += MathUtils.random(-dropVib, dropVib) * Gdx.graphics.getDeltaTime();
            if (raindrop3.y + 64 < 0) {
                dropleaks++;
                iter3.remove();
            }
            if (raindrop3.overlaps(player1.getRectangle())) {
                dropSound.play();
                player1Score++;
                if (player1Score % 5 == 0) {
                    player1Speed += 50;
                }
                iter3.remove();

            }
        }

        for (Iterator<Rectangle> iter4 = raindrops4.iterator(); iter4.hasNext(); ) {
            Rectangle raindrop4 = iter4.next();
            raindrop4.y -= dropSpeed * Gdx.graphics.getDeltaTime();
            raindrop4.x += MathUtils.random(-dropVib, dropVib) * Gdx.graphics.getDeltaTime();
            if (raindrop4.y + 64 < 0) {
                dropleaks++;
                iter4.remove();
            }

            if (raindrop4.overlaps(player1.getRectangle())) {
                dropSound.play();
                player1Score++;
                if (player1Score % 5 == 0) {
                    player1Speed += 50;
                }
                iter4.remove();
            }

        }

        ScreenUtils.clear(0, 0, 0.3f, 1);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(Background,0,0);

        for (Rectangle raindrop1 : raindrops1) {
            game.batch.draw(dropImage1, raindrop1.x, raindrop1.y);
        }
        for (Rectangle raindrop2 : raindrops2) {
            game.batch.draw(dropImage2, raindrop2.x, raindrop2.y);
        }

        for (Rectangle raindrop3 : raindrops3) {
            game.batch.draw(dropImage3, raindrop3.x, raindrop3.y);
        }

        for (Rectangle raindrop4 : raindrops4) {
            game.batch.draw(dropImage4, raindrop4.x, raindrop4.y);
        }

        game.batch.draw(player1.getTexture(), player1.getRectangle().x, player1.getRectangle().y);
        game.font.draw(game.batch, "P1's score: "+ player1Score + ", speed: "+ player1Speed + "\n"
                + "Drop leaks: "+ dropleaks, 50, 460);
        game.batch.end();

    }
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        dropImage1.dispose();
        dropImage2.dispose();
        dropImage3.dispose();
        dropImage4.dispose();
        player1.getTexture().dispose();
        dropSound.dispose();
        rainMusic.dispose();
        Background.dispose();
    }

}