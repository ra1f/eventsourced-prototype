package rxjava;

import rx.Observable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dueerkopra on 27.03.2015.
 */
public class RxJavaSample {

  static class User {
    String name;
    List<Video> videos;
    User(String name, List<Video> videos) {
      this.name = name;
      this.videos = videos;
    }
  }

  static class Video {
    String id;
    Rating rating;
    Video(String id, Rating rating) {
      this.id = id;
      this.rating = rating;
    }
  }

  static class Rating {
    Integer rating;
    Rating(Integer rating) {
      this.rating = rating;
    }
  }

  interface VideoService {
    Observable<Video> getVideos(User user);
    Observable<Rating> getRating(Video video);
  }

  static class Videos implements VideoService {

    @Override
    public Observable<Video> getVideos(User user) {
      return Observable.from(user.videos);
    }

    @Override
    public Observable<Rating> getRating(Video video) {
      return Observable.just(video.rating);
    }
  }

  public static void main(String[] args) throws Exception {

    Videos videos = new Videos();
    Observable<Video> videoObservable = videos.getVideos(new User("joe", Arrays.asList(new Video("V1", new Rating(9)), new Video("V2", new Rating(3)))));
    Observable<Rating> ratings = videoObservable.flatMap(v -> videos.getRating(v));
    ratings.subscribe(rating -> System.out.println(rating.rating));
  }
}
