package mini.project.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import mini.project.handler.MovieHandler;
import mini.project.util.Prompt;
import mini.project.util.Screen;

public class Member {

  private static final int FEMALE = 0;
  private static final int MALE = 1;

  String name;
  String ID;
  String password;
  Genre favoriteGenre;
  List<Movie> toWatchList = new ArrayList<>();
  MovieHandler toWatchHandler = new MovieHandler(toWatchList);
  List<Movie> watchedList = new Stack<>();
  MovieHandler watchedHandler = new MovieHandler(watchedList);

  public void setToWatchHandler(MovieHandler toWatchHandler) {
    this.toWatchHandler = toWatchHandler;
  }

  public void setWatchedHandler(MovieHandler watchedHandler) {
    this.watchedHandler = watchedHandler;
  }

  public MovieHandler getWatchedHandler() {
    return watchedHandler;
  }

  public List<Movie> getWatchedList() {
    return watchedList;
  }

  public MovieHandler getToWatchHandler() {
    return toWatchHandler;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public Genre getFavoriteGenre() {
    return favoriteGenre;
  }

  public void setFavoriteGenre(Genre favoriteGenre) {
    this.favoriteGenre = favoriteGenre;
  }

  public List<Movie> getToWatchList() {
    return toWatchList;
  }

  public void removeToWatch(Movie movie) {
    if (toWatchHandler.findByTitle(movie.getTitle()) != null) {
      toWatchList.remove(toWatchHandler.indexOf(movie.getTitle()));
    }
  }

  public void addWatched(Movie movie) {
    if (watchedHandler.findByTitle(movie.getTitle()) == null) {
      watchedList.add(movie);
    }
  }

  public void printToWatchList() {
    Screen.logo("To Watch List");
    toWatchHandler.list();
  }

  public void printHistory() throws InterruptedException {
    Screen.logo("HISTORY");
    watchedHandler.list();
  }

  public void watch(MovieHandler movieHandler) throws InterruptedException {
    Movie movie;

    while (true) {
      movie = movieHandler.findByTitle(Prompt.inputString("무슨 영화를 보시겠습니까? "));
      if (movie == null) {
        System.out.println("해당 제목의 영화를 찾지 못하였습니다.\n다시 입력해주세요.");
      } else {
        break;
      }
    }

    Screen.getWatchScreen(movie.getGenre());
    // 보고싶어요에 항목 삭제
    removeToWatch(movie);
    // 조회수 올리기
    movie.setViewCount(movie.getViewCount() + 1);
    // 본 영화에 추가
    addWatched(movie);
    // 취향장르 계산
    calculateFavoriteGenre();
  }



  public void calculateFavoriteGenre() {
    int[] genreCount = new int[4];
    for (int i = 0; i < watchedList.size(); i++) {
      switch (watchedList.get(i).getGenre()) {
        case 로맨스:
          genreCount[0]++;
          break;
        case 액션:
          genreCount[1]++;
          break;
        case 호러:
          genreCount[2]++;
          break;
        case 가족:
          genreCount[3]++;
      }
    }
    int index = 0;
    int max = genreCount[0];
    for (int i = 0; i < genreCount.length; i++) {
      if (genreCount[i] > max) {
        max = genreCount[i];
        index = i;
      }
    }
    switch (index) {
      case 0:
        setFavoriteGenre(Genre.로맨스);
        break;
      case 1:
        setFavoriteGenre(Genre.액션);
        break;
      case 2:
        setFavoriteGenre(Genre.호러);
        break;
      case 3:
        setFavoriteGenre(Genre.가족);

    }

  }

  public static Member valueOfCsv(String record, MovieHandler movieHandler) {
    String[] values = record.split(",");
    Member member = new Member();
    member.setName(values[0]);
    member.setID(values[1]);
    member.setPassword(values[2]);
    member.setFavoriteGenre(Genre.valueOf(values[3]));
    String[] toWatchs;
    if (!values[4].equals(" ")) {
      toWatchs = values[4].split(":");
      for (String title : toWatchs) {
        member.getToWatchList().add(movieHandler.findByTitle(title));
      }
      member.setToWatchHandler(new MovieHandler(member.getToWatchList()));
    }
    String[] watcheds;
    if (!values[5].equals(" ")) {
      watcheds = values[5].split(":");
      for (String title : watcheds) {
        member.getWatchedList().add(movieHandler.findByTitle(title));
      }
      member.setWatchedHandler(new MovieHandler(member.getWatchedList()));
    }

    return member;
  }

  public String toCsvString() {
    StringBuilder toWatch = new StringBuilder();
    StringBuilder watched = new StringBuilder();

    for (int i = 0; i < getToWatchList().size(); i++) {
      if (i != 0)
        toWatch.append(":");
      toWatch.append(getToWatchList().get(i).getTitle());
    }
    if (getToWatchList().size() == 0) {
      toWatch.append(" ");
    }
    for (int i = 0; i < getWatchedList().size(); i++) {
      if (i != 0)
        watched.append(":");
      watched.append(getWatchedList().get(i).getTitle());
    }
    if (getWatchedList().size() == 0) {
      watched.append(" ");
    }
    return String.format("%s,%s,%s,%s,%s,%s\n", getName(), getID(), getPassword(),
        getFavoriteGenre(), toWatch.toString(), watched.toString());
  }

}

