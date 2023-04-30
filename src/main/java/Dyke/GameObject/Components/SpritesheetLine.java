package Dyke.GameObject.Components;

public class SpritesheetLine {
    int width, height, number, spacing;
    public SpritesheetLine(int spriteWidth, int spriteHeight, int numSprites, int spacing){
        this.width = spriteWidth;
        this.height = spriteHeight;
        this.number = numSprites;
        this.spacing = spacing;
    }
}
