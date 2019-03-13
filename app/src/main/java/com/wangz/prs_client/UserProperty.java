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

        private String id;
        private String name;
        private String type;
        private String occupier;
        private String location;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOccupier() {
            return occupier;
        }

        public void setOccupier(String occupier) {
            this.occupier = occupier;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
