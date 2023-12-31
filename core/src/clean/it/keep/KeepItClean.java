package clean.it.keep;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class KeepItClean extends Game {
	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	public void changeToTutorialScreen() {
		setScreen(new TutorialScreen(this));
	}

	public void changeToGameScreen() {
		setScreen(new GameScreen(this));
	}

	public void changeToMainMenuScreen() {
		setScreen(new MainMenuScreen(this));
	}

	public void changeToGameOverScreen() {
		setScreen(new GameOverScreen(this));
	}

}
