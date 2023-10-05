package clean.it.keep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
public class Player {
    private final Rectangle rectangle = new Rectangle();
    private Texture texture;
    private Texture[] textures;
    private int currentTextureIndex = 0;

public Player(int x, int y, int width, int height, String[] paths) {
    this.rectangle.x = x;
    this.rectangle.y = y;
    this.rectangle.width = width;
    this.rectangle.height = height;
    this.textures = new Texture[paths.length];

    for (int i = 0; i < paths.length; i++) {
        this.textures[i] = new Texture(Gdx.files.internal(paths[i]));
    }

    this.texture = this.textures[currentTextureIndex]; // Set the initial texture
}

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexture(int textureIndex) {
        this.texture = textures[textureIndex];
    }

}
