package Dyke.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private String filePath;
    private boolean beingUsed = true;

    public Shader(String filepath){
        this.filePath = filepath;
        try{

            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)()*([a-zA-Z])*");

            int startIndex = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n",startIndex);
            String firstPattern = source.substring(startIndex, eol).trim();
            startIndex = source.indexOf("#type",startIndex) + 6;
            eol = source.indexOf("\r\n",startIndex);
            String secondPattern = source.substring(startIndex, eol).trim();

            if(firstPattern.equals("vertex")){
                vertexSource = splitString[1];
                fragmentSource = splitString[2];
            }else if (firstPattern.equals("fragment")){
                vertexSource = splitString[2];
                fragmentSource = splitString[1];
            }else{
                throw new IOException("Unexpected token in shader file");
            }
            vertexSource = vertexSource.substring(vertexSource.indexOf("\r\n"));
            fragmentSource = fragmentSource.substring(fragmentSource.indexOf("\r\n"));

        }catch (IOException e){
            e.printStackTrace();
            assert false : "ERROR: Could not open file for shader'" +filepath + "'";
        }
    }

    public void compile(){
        int vertexID,fragmentID;

        //Compile and link shaders

        //First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filePath+"'.glsl\n\tVertex shader compilation failed'");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false: "";
        }

        //Next load the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filePath+"'.glsl\n\tFragment shader compilation failed'");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false: "";
        }

        //Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        success = glGetProgrami(shaderProgramID,GL_LINK_STATUS);
        if(success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
            System.out.println("ERROR: '"+filePath+"'.glsl\n\tLinking of shaders failed'");
            System.out.println(glGetProgramInfoLog(shaderProgramID,len));
            assert false: "";
        }


    }

    public void use(){
        //Bind shader program
        glUseProgram(shaderProgramID);
        beingUsed = true;
    }

    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16); // Mat4
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false,matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec4f){
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform4f(varLocation, vec4f.x, vec4f.y, vec4f.z, vec4f.w);
    }

    public void uploadFloat(String varName, float f){
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1f(varLocation, f);
    }

    public void uploadInt(String varName, int i){
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1i(varLocation, i);
    }
    public void uploadMat3f(String varName, Matrix3f mat3f){
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3f.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec2f(String varName, Vector2f vec2f){
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform2f(varLocation, vec2f.x, vec2f.y);
    }

    public void uploadTexture(String varName, int slot){
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1i(varLocation, slot);
    }
}
