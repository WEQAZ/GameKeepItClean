package clean.it.keep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;

public class GameScreen implements Screen {

    final KeepItClean game;
    private OrthographicCamera camera;
    private Texture dropImage1;
    private Texture dropImage2;
    private Texture dropImage3;
    private Texture dropImage4;
    private Sound dropSound;
    private Sound failDropSound;
    private Music rainMusic;
    private Player player1;
    private Array<TrashDrop> trashDrops;
    private int player1Score = 0;
    private int player1Speed = 500;
    private int colorCode;
    private int dropSpeed = 200;
    private int dropVib = 10;
    private int dropleaks = 0;
    private int lifePoint = 10;
    final Texture Background;
    private Texture lifePointImage;
    private int highestScore = 0;

    public GameScreen(final KeepItClean game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Define an array of file paths for player1's image resources.
        String[] paths = {"blueBin.png", "redBin.png", "greenBin.png", "yellowBin.png"};
        // Create a Player object with specific position, and image paths.
        player1 = new Player(704, 20, 64, 64, paths);


        // Create a Texture for the first type of trash drop (e.g., banana)
        dropImage1 = new Texture(Gdx.files.internal("banana.png"));

        // Create a Texture for the second type of trash drop (e.g., battery)
        dropImage2 = new Texture(Gdx.files.internal("battery.png"));

        // Create a Texture for the third type of trash drop (e.g., glass bottle)
        dropImage3 = new Texture(Gdx.files.internal("glass-bottle.png"));

        // Create a Texture for the fourth type of trash drop (e.g., plastic bag)
        dropImage4 = new Texture(Gdx.files.internal("plastic-bag.png"));

        // Initialize an Array to store TrashDrop instances (representing falling trash objects)
        trashDrops = new Array<TrashDrop>();

        dropSound = Gdx.audio.newSound(Gdx.files.internal("garbageSoundEffect.wav"));
        failDropSound = Gdx.audio.newSound(Gdx.files.internal("IncorrectSoundEffect.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("bgMusic.mp3"));
        Background = new Texture(Gdx.files.internal("gameScreen.png"));
        lifePointImage = new Texture(Gdx.files.internal("lifePointImage.png"));
        // set high score = 0
        highestScore = loadHighScore();
        rainMusic.setLooping(true);
    }

    // Saves the player's high score to device preferences.
    private void saveHighScore(int score) {
        Preferences prefs = Gdx.app.getPreferences("MyGamePreferences");
        prefs.putInteger("highScore", score);
        prefs.flush();
    }

    // Method to load the highest score
    private int loadHighScore() {
        Preferences prefs = Gdx.app.getPreferences("MyGamePreferences");
        return prefs.getInteger("highScore", 0); // Default to 0 if no high score is saved
    }

    // update high score
    private void updateHighScore() {
        if (player1Score > highestScore) {
            highestScore = player1Score;
            saveHighScore(highestScore);
        }
    }

    private void spawnTrashDrop() {
        Texture[] trashTextures = {dropImage1, dropImage2, dropImage3, dropImage4};
        Texture selectedTexture = trashTextures[MathUtils.random(trashTextures.length - 1)];

        int trashType = -1; // Default value, or any invalid value

        if (selectedTexture == dropImage1) {
            trashType = 0; // Set the trash type for banana
        } else if (selectedTexture == dropImage2) {
            trashType = 1; // Set the trash type for battery
        } else if (selectedTexture == dropImage3) {
            trashType = 2; // Set the trash type for bottle
        } else if (selectedTexture == dropImage4) {
            trashType = 3; // Set the trash type for  bag
        }

        TrashDrop trashDrop = new TrashDrop(selectedTexture, trashType);
        trashDrops.add(trashDrop);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.4f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // if user press key it gonna change color that user want
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            colorCode = 1;
            // player1.setTexture(1); // Change to the redBin.png texture (index 1)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            colorCode = 0;
            // player1.setTexture(0); // Change to the buleBin.png texture (index 0)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            colorCode = 2;
            // player1.setTexture(2); // Change to the greenBin.png texture (index 2)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            colorCode = 3;
            // player1.setTexture(3); // Change to the yellowBin.png texture (index 3)
        }
        player1.setTexture(colorCode);


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            player1.getRectangle().x -= player1Speed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            player1.getRectangle().x += player1Speed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            player1.getRectangle().y += player1Speed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            player1.getRectangle().y -= player1Speed * Gdx.graphics.getDeltaTime();

        if (player1.getRectangle().x < 0) player1.getRectangle().x = 0;
        if (player1.getRectangle().x > 800 - 64) player1.getRectangle().x = 800 - 64;
        if (player1.getRectangle().y < 0) player1.getRectangle().y = 0;
        if (player1.getRectangle().y > 480 - 64) player1.getRectangle().y = 480 - 64;

        if (MathUtils.random(0, 1000) < 10) {
            spawnTrashDrop();
        }

        for (Iterator<TrashDrop> iter = trashDrops.iterator(); iter.hasNext(); ) {
            TrashDrop trashDrop = iter.next();
            trashDrop.update(Gdx.graphics.getDeltaTime(), dropSpeed, dropVib);
            if (trashDrop.isOutOfBounds()) {
                dropleaks++;
                iter.remove();
            }

            if (trashDrop.getRectangle().overlaps(player1.getRectangle())) {
                int trashType = trashDrop.getTrashType();
                switch (colorCode) {
                    case 0: // Blue (banana)
                        if (trashType == 0) {
                            dropSound.play();
                            player1Score++;
                        } else {
                            failDropSound.play();
                            lifePoint--;
                        }
                        break;

                    case 1: // Red (battery)
                        if (trashType == 1) {
                            dropSound.play();
                            player1Score++;
                        } else {
                            failDropSound.play();
                            lifePoint--;
                        }
                        break;

                    case 2: // Green (bottle)
                        if (trashType == 2) {
                            dropSound.play();
                            player1Score++;
                        } else {
                            failDropSound.play();
                            lifePoint--;
                        }
                        break;

                    case 3: // Yellow (bag)
                        if (trashType == 3) {
                            dropSound.play();
                            player1Score++;
                        } else {
                            failDropSound.play();
                            lifePoint--;
                        }
                        break;

                    default:
                        lifePoint--; // Default case for other colors
                        break;
                }
                if (player1Score % 5 == 0) {
                    player1Speed += 50;
                }
                iter.remove();
            }
        }

        ScreenUtils.clear(0, 0, 0.3f, 1);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Check if the player's life points have fallen below zero.
        if (lifePoint < 0) {
            // change to game over screen.
            game.changeToGameOverScreen();
            dispose();
        }
        game.batch.draw(Background, 0, 0);

        for (TrashDrop trashDrop : trashDrops) {
            Texture trashTexture = trashDrop.getTexture();
            float x = trashDrop.getRectangle().x;
            float y = trashDrop.getRectangle().y;
            game.batch.draw(trashTexture, x, y);
        }


        game.batch.draw(player1.getTexture(), player1.getRectangle().x, player1.getRectangle().y);
        game.font.draw(game.batch, "score: " + player1Score + ", speed: " + player1Speed + "\n" + "Drop leaks: " + dropleaks + "\n" + "Life point: " + lifePoint + "\n" + "High score: " + highestScore, 25, 440);

        // draw life point image
        float lifePointX = 20; // Adjust the X-coordinate as needed
        for (int i = 0; i < lifePoint; i++) {
            game.batch.draw(lifePointImage, lifePointX, 450);
            lifePointX += lifePointImage.getWidth() + 2; // Add spacing between life point images
        }

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
