package hello.core.sington;

public class SingletonService {

    // 1. private static 영역에 객체 1개만 생성
    private static final SingletonService instance = new SingletonService();

    // 2. public으로. 만약 1번 생성 객체를 조회하고싶으면 반드시 여기 메서드를 통해서만 접근할 수 있도록 한다.
    public static SingletonService getInstance() {
        return instance;
    }
    // 3. 생성자를 private 로 선언하여 외부에서 new 키워드를 사용한 객체 생성을 막는다.
    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직  호출");
    }
//    public static void main(String[] args) {
//        SingletonService singletonService1 = new SingletonService();
//        SingletonService singletonService2 = new SingletonService();
//    }
}
