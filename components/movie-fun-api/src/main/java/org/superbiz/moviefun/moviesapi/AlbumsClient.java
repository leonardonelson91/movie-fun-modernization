package org.superbiz.moviefun.moviesapi;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestOperations;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class AlbumsClient {

    private String albumServiceUrl;
    private RestOperations restOperations;
    private static ParameterizedTypeReference<List<AlbumInfo>> movieListType = new ParameterizedTypeReference<List<AlbumInfo>>() {
    };

    public AlbumsClient(String albumServiceUrl, RestOperations restOperations) {
        this.albumServiceUrl = albumServiceUrl;
        this.restOperations = restOperations;
    }

    public void addAlbum(AlbumInfo album) {
        restOperations.postForLocation(albumServiceUrl, album);
    }

    public AlbumInfo find(long id) {
        return restOperations.getForObject(albumServiceUrl + "/" + id, AlbumInfo.class);
    }

    public List<AlbumInfo> getAlbums() {
        return restOperations.exchange(albumServiceUrl, GET, null, movieListType).getBody();
    }
}
