package com.pintogether.backend.service;
public interface DomainType {
    String getName();

    enum Collection implements DomainType {
        THUMBNAIL;

        @Override
        public String getName() {
            return "collection/" + this.name();
        }
    }

    enum Member implements DomainType {
        AVATAR;

        @Override
        public String getName() {
            return "member/" + this.name();
        }
    }

    enum Pin implements DomainType {
        REVIEW_IMAGE;

        @Override
        public String getName() {
            return "pin/" + this.name();
        }
    }
}

