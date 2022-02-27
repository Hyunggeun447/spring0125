package study.datajpa.repository;

import lombok.Getter;

/**
 * projections
 */

@Getter
public class UserNameOnlyDto {

    private final String userName;

    public UserNameOnlyDto(String userName) {
        this.userName = userName;
    }


}
