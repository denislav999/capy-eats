package capyeats.email;

import capyeats.user.model.User;
import capyeats.user.model.UserRole;
import capyeats.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserService userService;

    @Autowired
    public EmailService(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }


    public void sendDailyAdminReportToAllAdmins() {
        Set<User> admins = userService.findAllByRole(UserRole.ADMIN);

        if (admins.isEmpty()) {
            System.out.println("No admins found for daily report.");
            return;
        }

        for (User admin : admins) {
            if (admin.getEmail() == null || admin.getEmail().isBlank()) {
                continue;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(admin.getEmail());
            message.setSubject("Daily CapyEats Report");
            message.setText("Hello " + admin.getUsername() + ",\n\nHere is your daily report.");

            try {
                mailSender.send(message);
            } catch (Exception e) {
                System.out.println("Failed to send email to: " + admin.getEmail());
            }
        }

        System.out.println("Daily report email sent to " + admins.size() + " admins.");
    }
}

