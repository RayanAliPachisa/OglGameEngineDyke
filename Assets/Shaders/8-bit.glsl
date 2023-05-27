    #type vertex
    #version 330 core 
        layout(location = 0) in vec3 aPos; 
        layout(location = 1) in vec4 aColor;
        layout(location = 2) in vec2 aTexCoords;
        layout(location = 3) in float aTexId;

        uniform mat4 uProjection;
        uniform mat4 uView;
        out vec2 fTexCoords;
        out float fTexId;
        out vec4 fColor; 

        void main(){ 
            fColor = vec4(aColor);
            gl_Position = uProjection * uView * vec4(aPos, 1.0);
            fTexId = aTexId;
            fTexCoords = aTexCoords;
        } 
        

    #type fragment
    #version 330 core
        in vec4 fColor;
        in vec2 fTexCoords;
        in float fTexId;

        uniform sampler2D uTextures[8];

        out vec4 color;

        void main(){
            if(fTexId > 0){
                int id = int(fTexId);
                color = fColor * texture(uTextures[id], fTexCoords);
            }else{
                color = fColor;
            }

            if(color.x > 0.5){
                color.x = 1;
            }else{
                color.x = 0;
            }

            if(color.y > 0.5){
                color.y = 1;
            }else{
                color.y = 0;
            }

            if(color.z > 0.5){
                color.z = 1;
            }else{
                color.z = 0;
            }



            /*if (color.w <= 0.0) {
                discard;
            }*/

        }