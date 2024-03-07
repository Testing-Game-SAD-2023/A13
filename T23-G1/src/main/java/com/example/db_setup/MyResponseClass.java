package com.example.db_setup;

import java.util.List;

public class MyResponseClass {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MyResponseClass{" +
                "data=" + data +
                '}';
    }

    public static class Data {
        private String app_id;
        private String type;
        private String application;
        private long data_access_expires_at;
        private long expires_at;
        private boolean is_valid;
        private List<String> scopes;
        private String user_id;

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public long getData_access_expires_at() {
            return data_access_expires_at;
        }

        public void setData_access_expires_at(long data_access_expires_at) {
            this.data_access_expires_at = data_access_expires_at;
        }

        public long getExpires_at() {
            return expires_at;
        }

        public void setExpires_at(long expires_at) {
            this.expires_at = expires_at;
        }

        public boolean isIs_valid() {
            return is_valid;
        }

        public void setIs_valid(boolean is_valid) {
            this.is_valid = is_valid;
        }

        public List<String> getScopes() {
            return scopes;
        }

        public void setScopes(List<String> scopes) {
            this.scopes = scopes;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "app_id='" + app_id + '\'' +
                    ", type='" + type + '\'' +
                    ", application='" + application + '\'' +
                    ", data_access_expires_at=" + data_access_expires_at +
                    ", expires_at=" + expires_at +
                    ", is_valid=" + is_valid +
                    ", scopes=" + scopes +
                    ", user_id='" + user_id + '\'' +
                    '}';
        }
    }
}