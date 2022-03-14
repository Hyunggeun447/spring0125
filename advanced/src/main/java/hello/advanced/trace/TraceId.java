package hello.advanced.trace;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TraceId {

    private String id;
    private int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    // 생성자 private으로 내부에서만 생성
    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    //UUid 길이가 길어서 맨 앞 8자리만 잘라냄
    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    //들여쓰기 표시할때 level 증가
    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }
    //들여쓰기 표시할때 level 감소
    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }


}
