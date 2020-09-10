/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package mini.project;

import mini.project.domain.Genre;
import mini.project.domain.Member;
import mini.project.domain.Movie;
import mini.project.handler.MemberHandler;
import mini.project.handler.MovieHandler;
import mini.project.util.ArrayList;
import mini.project.util.List;
import mini.project.util.Prompt;
import mini.project.util.Screen;


public class App {
  // Screen.BeforeSignUpScreen()
  // 회원가입 권유
  // Screen.AfterSignUpScereen()
  // 메뉴처리(MemberHandler, MovieHandler)



  public static void main(String[] args) throws InterruptedException {
    List<Movie> movieList = new ArrayList<>();
    MovieHandler movieHandler = new MovieHandler(movieList);
    List<Member> memberList = new ArrayList<>();
    MemberHandler memberHandler = new MemberHandler(memberList, movieHandler);

    // 로그인한 사용자
    Member loggedInMember = null;



    for (int i = 0; i < 20; i++) {
      Movie movie;
      if (i < 5) {
        movie = new Movie(String.format("%d", i), Genre.로맨스);
        movie.setViewCount(1);
      } else if (i < 10) {
        movie = new Movie(String.format("%d", i), Genre.액션);
        movie.setViewCount(2);
      } else if (i < 15) {
        movie = new Movie(String.format("%d", i), Genre.호러);
        movie.setViewCount(3);
      } else {
        movie = new Movie(String.format("%d", i), Genre.가족);
        movie.setViewCount(4);
      }
      movieList.add(movie);
    }



    /*
     * if (Prompt.inputString("회원가입하시겠습니까?(y/N)").equalsIgnoreCase("y")) { loggedInMember =
     * memberHandler.add(); } else { System.out.println("프로그램을 종료합니다."); return; }
     */

    loop: while (true) {

      // 사용자가 입력한 명령을 보관한다.
      if (loggedInMember == null) {
        Screen.bitflixLogo();
        movieHandler.printBest();
        Screen.viewMenu(Screen.BEFORE_LOGIN_PAGE);
        switch (Prompt.inputString("명령> ")) {
          case "회원가입":
            memberHandler.add();
            break;
          case "로그인":
            loggedInMember = memberHandler.findByID(Prompt.inputString("아이디를 입력하세요."));
            break;
          case "관리자":
            Screen.viewMenu(Screen.MANAGER_PAGE);
            switch (Prompt.inputString("명령> ")) {
              case "회원관리":

                memberHandler.manage();
                break;
              case "영화관리":
                Screen.viewMenu(Screen.MOVIE_MANAGE_PAGE);
                movieHandler.manage();
                break;
              case "종료":
                System.out.println("프로그램을 종료합니다.");
                return;
              default:
                System.out.println("잘못된 명령입니다.");
            }
            break;
          case "종료":
            System.out.println("프로그램을 종료합니다.");
            break loop;
          default:
            System.out.println("잘못된 명령입니다.");

        }
      }
      if (loggedInMember != null) {

        Screen.bitflixLogo();
        movieHandler.printGenre(loggedInMember.getFavoriteGenre());
        movieHandler.printBest();
        Screen.viewMenu(Screen.AFTER_LOGIN_PAGE);

        switch (Prompt.inputString("명령> ")) {
          case "로그아웃":
            if (Prompt.inputString("정말 로그아웃 하시겠습니까?(y/N)").equalsIgnoreCase("y"))
              loggedInMember = null;
            break;

          case "영화시청":
            loggedInMember.watch(movieHandler);
            break;
          case "보고싶어요":
            loggedInMember.printToWatchList();
            if (Prompt.inputString("영화를 보시겠습니까?(y/N)").equalsIgnoreCase("y")) {
              loggedInMember.watch(movieHandler);
            }
            break;
          case "장르별 더보기":
            movieHandler.printGenre(Prompt.inputGenre("로맨스, 액션, 가족, 호러\n장르? "));
            if (Prompt.inputString("영화를 보시겠습니까?(y/N)").equalsIgnoreCase("y")) {
              loggedInMember.watch(movieHandler);
            }
            break;
          case "인기순 더보기":
            movieHandler.printBest();
            if (Prompt.inputString("영화를 보시겠습니까?(y/N)").equalsIgnoreCase("y")) {
              loggedInMember.watch(movieHandler);
            }
            break;
          case "다시보기":
            loggedInMember.printHistory();
            if (Prompt.inputString("영화를 보시겠습니까?(y/N)").equalsIgnoreCase("y")) {
              loggedInMember.watch(loggedInMember.getWatchedHandler());
            }
            break;
          case "종료":
            System.out.println("프로그램을 종료합니다.");
            break loop;
          default:
            System.out.println("잘못된 명령입니다.");
        }

      }
    }
  }


  /*
   *
   * System.out.println("실행할 수 없는 명령입니다.");
   */
}
// System.out.println(); // 이전 명령의 실행을 구분하기 위해 빈 줄 출력


