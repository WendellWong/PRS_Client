package com.wangz.prs_client;

import java.util.List;

public class UserProperty {

    private List<PayloadBean> payload;

    public List<PayloadBean> getPayload() {
        return payload;
    }

    public void setPayload(List<PayloadBean> payload) {
        this.payload = payload;
    }

    public static class PayloadBean {
        /**
         * id : 2
         * name : NEO_TEST_OBJ
         * type : -100
         * occupier : 0
         * location : 5
         * status : 1
         */

        private int id;
        private String name;
        private int type;
        private int occupier;
        private int location;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getOccupier() {
            return occupier;
        }

        public void setOccupier(int occupier) {
            this.occupier = occupier;
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
