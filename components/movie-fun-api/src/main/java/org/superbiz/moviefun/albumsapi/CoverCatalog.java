package org.superbiz.moviefun.albumsapi;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class CoverCatalog {

    private BlobStore blobStore;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CoverCatalog(BlobStore blobStore) {
        this.blobStore = blobStore;
    }

    @HystrixCommand(fallbackMethod = "buildDefaultCoverBlob")
    public Blob getCover(long albumId) throws IOException {
        Blob coverBlob = null;
        Optional<Blob> maybeCoverBlob = Optional.empty();

        maybeCoverBlob = blobStore.get(getCoverBlobName(albumId));
        coverBlob = maybeCoverBlob.orElseGet(this::buildDefaultCoverBlob);


        return coverBlob;
    }

    public void uploadCover(@PathVariable Long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {
        logger.debug("Uploading cover for album with id {}", albumId);

        Blob coverBlob = new Blob(
                getCoverBlobName(albumId),
                uploadedFile.getInputStream(),
                uploadedFile.getContentType()
        );

        blobStore.put(coverBlob);
    }

    private Blob buildDefaultCoverBlob() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream input = classLoader.getResourceAsStream("default-cover.jpg");
        byte[] imageBytes = new byte[0];
        try {
            imageBytes = IOUtils.toByteArray(classLoader.getResourceAsStream("default-cover.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Blob("default-cover", input, MediaType.IMAGE_JPEG_VALUE, imageBytes.length);
    }

    private String getCoverBlobName(@PathVariable long albumId) {
        return format("covers/%d", albumId);
    }

    /**
     * The fallback method to return a default cover if blobstore doesnt work
     * @param albumId
     * @return the default blob
     */
    public Blob buildDefaultCoverBlob(long albumId) {
        return this.buildDefaultCoverBlob();
    }
}
