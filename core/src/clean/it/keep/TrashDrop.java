package clean.it.keep;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class TrashDrop {
    private Rectangle rectangle;
    private long lastDropTime;
    private Texture texture; // Add a field to store the Texture for this TrashDrop
    private int trashType;

    // Constructor for creating a TrashDrop instance
    public TrashDrop(Texture texture, int trashType) {
        this.texture = texture; // Set the Texture for this TrashDrop
        this.trashType = trashType;
        rectangle = new Rectangle();
        rectangle.width = 64;
        rectangle.height = 64;
        spawn();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    // Method to spawn the trash drop at a random position
    public void spawn() {
        rectangle.x = MathUtils.random(0, 800 - 64);
        rectangle.y = 480;
        lastDropTime = TimeUtils.nanoTime();
    }

    // Update method to move the trash drop based on deltaTime, dropSpeed, and dropVib
    public void update(float deltaTime, int dropSpeed, int dropVib) {
        rectangle.y -= dropSpeed * deltaTime;
        rectangle.x += MathUtils.random(-dropVib, dropVib) * deltaTime;
    }

    // Check if the trash drop is out of bounds (above the screen)
    public boolean isOutOfBounds() {
        return rectangle.y + 64 < 0;
    }

    public Texture getTexture() {
        return texture; // Return the stored Texture for this TrashDrop
    }

    public int getTrashType() {
        return trashType; // Getter method to retrieve the trash type
    }
}



