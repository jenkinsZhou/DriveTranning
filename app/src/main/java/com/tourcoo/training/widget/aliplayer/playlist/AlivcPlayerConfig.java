package com.tourcoo.training.widget.aliplayer.playlist;

/**
 * 对外提供的配置文件
 */
public class AlivcPlayerConfig {

    private String vid;

    public static class Builder{

        private AlivcPlayerConfig alivcPlayerConfig;

        private String vid;

        private Builder(){
            alivcPlayerConfig = new AlivcPlayerConfig();
        }

        public Builder vid(String vid){
            alivcPlayerConfig.vid = vid;
            return this;
        }

        public AlivcPlayerConfig build(){
            return alivcPlayerConfig;
        }
    }

    public String getVid() {
        return vid;
    }
}
