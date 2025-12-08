package capyeats.email;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Async
    @EventListener
    public void sendEmail() {
        System.out.println("Sending email...");
    }
}
