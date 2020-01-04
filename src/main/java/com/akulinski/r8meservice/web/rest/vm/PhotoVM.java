package com.akulinski.r8meservice.web.rest.vm;

import java.io.Serializable;
import java.util.Objects;

public class PhotoVM implements Serializable {
    private final  String link;

    public PhotoVM(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoVM)) return false;
        PhotoVM photoVM = (PhotoVM) o;
        return Objects.equals(getLink(), photoVM.getLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLink());
    }

    @Override
    public String toString() {
        return "PhotoVM{" +
            "link='" + link + '\'' +
            '}';
    }
}
