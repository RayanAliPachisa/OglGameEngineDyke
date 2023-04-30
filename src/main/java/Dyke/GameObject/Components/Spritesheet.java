package Dyke.GameObject.Components;

import Dyke.renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    private Texture texture;
    public List<Sprite> sprites;
    public Spritesheet (Texture texture, int spriteWidth, int spriteHeight, int numSpritesLine, int lines, int spacing){
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight(); // TL of first sprite

        for (int line  = 0; line < lines; line++){
            currentY = currentY - spriteHeight;
            for(int i = 0; i < numSpritesLine; i++){
                float topY = currentY + spriteHeight; // Getting bottom left and top right coreners of sprite in texture
                float rightX = currentX + spriteWidth;
                float leftX = currentX;
                float bottomY = currentY;
                //Converting to normalised device coords
                topY = topY/(float)texture.getHeight();
                bottomY = bottomY/(float)texture.getHeight();
                leftX = leftX/(float)texture.getWidth();
                rightX = rightX/(float)texture.getWidth();


                currentX += spriteWidth + spacing;

                Vector2f[] texCoords = {
                        new Vector2f(rightX, topY),
                        new Vector2f(rightX, bottomY),
                        new Vector2f(leftX, bottomY),
                        new Vector2f(leftX, topY)
                };
                //creating sprite and adding to the sprites list
                sprites.add(new Sprite(texture, texCoords));
            }
        }
    }

    public Spritesheet(Texture texture, SpritesheetLine... spritesheetLines){
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight(); // TL of first sprite

        for (SpritesheetLine line: spritesheetLines){
            currentY = currentY - line.height;
            for(int i = 0; i < line.number; i++){
                float topY = currentY + line.height; // Getting bottom left and top right coreners of sprite in texture
                float rightX = currentX + line.width;
                float leftX = currentX;
                float bottomY = currentY;
                //Converting to normalised device coords
                topY = topY/(float)texture.getHeight();
                bottomY = bottomY/(float)texture.getHeight();
                leftX = leftX/(float)texture.getWidth();
                rightX = rightX/(float)texture.getWidth();


                currentX += line.width + line.spacing;

                Vector2f[] texCoords = {
                        new Vector2f(rightX, topY),
                        new Vector2f(rightX, bottomY),
                        new Vector2f(leftX, bottomY),
                        new Vector2f(leftX, topY)
                };
                //creating sprite and adding to the sprites list
                sprites.add(new Sprite(texture, texCoords));
            }
        }
    }
}
