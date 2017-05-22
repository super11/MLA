package com.mla.newsapp.config;

/**
 * Created by manish.patwari on 5/8/15.
 */
public class Constants {

    public Constants(){

    }
    public static enum NewsType{
        LIST("list"), VIDEOS("videos"),GALLERY("gallery") ;

        private final String type;

        private NewsType(String type){
            this.type = type;
        }

        @Override
        public String toString(){
            return type;
        }
    }

    public static enum IntentActionType{
        CATEGORY_CHANGE("all");

        private final String category;

        private IntentActionType(String category){
            this.category = category;
        }

        @Override
        public String toString(){
            return category;
        }
    }

    public static enum IntentExtraKey{
        CATEGORY("category");

        private final String category;

        private IntentExtraKey(String category){
            this.category = category;
        }

        @Override
        public String toString(){
            return category;
        }
    }
}
