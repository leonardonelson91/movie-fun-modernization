package org.superbiz.moviefun.blobstore;

import java.io.InputStream;

public class Blob {
    public final String name;
    public final InputStream inputStream;
    public final String contentType;
    public final Integer size;

    public Blob(String name, InputStream inputStream, String contentType, Integer size) {
        this.name = name;
        this.inputStream = inputStream;
        this.contentType = contentType;
        this.size = size;
    }

    public Blob(String name, InputStream inputStream, String contentType) {
        this(name, inputStream, contentType, 0);
    }
}
