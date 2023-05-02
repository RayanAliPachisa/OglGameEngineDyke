package Dyke.util;

import Dyke.GameObject.Components.Graphical.Spritesheet;
import Dyke.renderer.Shader;
import Dyke.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spriteSheets = new HashMap<>();

    public static Shader getShader(String resourceName){return getShader(resourceName, false);}
    public static Shader getShader(String resourceName, Boolean isFullPath){
        File file = null;
        if(isFullPath){
            file = new File(resourceName);
        }else {
            file = new File("Assets/Shaders/" + resourceName);
        }
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())){
            //return shader if shader is there
            return AssetPool.shaders.get(file.getAbsolutePath());
        }else{
            //Put in hashmap if shader not there
            Shader shader = new Shader(isFullPath ? resourceName:"Assets/Shaders/" + resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Shader getTexture(String resourceName){return getShader(resourceName, false);}

    public static Texture getTexture(String resourceName, boolean isFullPath){
        File file = null;
        if(isFullPath){
            file = new File(resourceName);
        }else {
            file = new File("Assets/Textures/" + resourceName);
        }
        if(AssetPool.textures.containsKey(file.getAbsolutePath())){
            //return shader if shader is there
            return AssetPool.textures.get(file.getAbsolutePath());
        }else{
            //Put in hashmap if shader not there
            Texture texture = new Texture(isFullPath ? resourceName:"Assets/Textures/" + resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet, boolean isFullPath) {
        File file = new File(resourceName);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())) {
            AssetPool.spriteSheets.put(isFullPath ? file.getAbsolutePath() : "Assets/Textures/" + file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName, boolean isFullPath){
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey( isFullPath ? file.getAbsolutePath() : "Assets/Textures/" + file.getAbsolutePath())){
            assert false:"Error: Tried to access spritesheet'" + resourceName + "' and it has not been added to asserpool";
        }
        return spriteSheets.get(isFullPath ? file.getAbsolutePath() : "Assets/Textures/" + file.getAbsolutePath());
    }
}
