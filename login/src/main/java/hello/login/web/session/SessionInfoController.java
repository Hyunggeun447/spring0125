package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "No session";
        }

        //session data
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name = {},value={}", name, session.getAttribute(name)));

        log.info("session id = {}", session.getId());
        log.info("session getMaxInactiveInterval = {}", session.getMaxInactiveInterval());
        log.info("session creationTime = {}", new Date(session.getCreationTime()));
        log.info("session lastAccessedTime = {}", new Date(session.getLastAccessedTime()));
        log.info("isNew = {}", session.isNew());
        return "print session";
    }
}
