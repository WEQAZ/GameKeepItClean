package clean.it.keep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final KeepItClean game;
    OrthographicCamera camera;

    private Texture redBin;
    private Texture yellowBin;
    private Texture greenBin;
    private Texture blueBin;
    private Texture howTo;

    public MainMenuScreen(final KeepItClean game) {
        this.game = game;
        redBin = new Texture(Gdx.files.internal("redBin.png"));
        blueBin = new Texture(Gdx.files.internal("blueBin.png"));
        greenBin = new Texture(Gdx.files.internal("greenBin.png"));
        yellowBin = new Texture(Gdx.files.internal("yellowBin.png"));
        howTo = new Texture(Gdx.files.internal("howTo.jpg"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(redBin,275,300);
        game.batch.draw(yellowBin,350,300);
        game.batch.draw(greenBin,425,300);
        game.batch.draw(blueBin,500,300);
        game.batch.draw(howTo,0,0);
        game.font.draw(game.batch, "Welcome to Keep It CLean!!! ", 335, 250);
        game.font.draw(game.batch, "Press [Spacebar] or Tap anywhere to begin!", 265, 210);
        game.font.draw(game.batch, "using Arrow keys to control", 335, 180);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
