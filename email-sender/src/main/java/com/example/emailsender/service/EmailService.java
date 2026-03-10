package com.example.emailsender.service;

import com.example.emailsender.dto.DailyReportEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String to) {
        String subject = "Welcome to Task Tracker!";
        String body = buildWelcomeEmailBody(to);
        sendHtmlEmail(to, subject, body);
        log.info("Welcome email sent to: {}", to);
    }

    public void sendDailyReport(String to, List<DailyReportEvent.TaskInfo> tasks) {
        String subject = "Your Daily Task Report";
        String body = buildDailyReportBody(to, tasks);
        sendHtmlEmail(to, subject, body);
        log.info("Daily report sent to: {} with {} tasks", to, tasks.size());
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildWelcomeEmailBody(String email) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <h2 style="color: #4A90E2;">Welcome to Task Tracker! 🎉</h2>
                    <p>Hi <strong>%s</strong>,</p>
                    <p>Your account has been successfully created.</p>
                    <p>You can now start managing your tasks efficiently.</p>
                    <br>
                    <p>Best regards,<br>Task Tracker Team</p>
                </body>
                </html>
                """.formatted(email);
    }

    private String buildDailyReportBody(String email, List<DailyReportEvent.TaskInfo> tasks) {
        StringBuilder taskRows = new StringBuilder();

        if (tasks.isEmpty()) {
            taskRows.append("<p>No tasks for today. Great job! ✅</p>");
        } else {
            taskRows.append("""
                    <table style="width: 100%; border-collapse: collapse;">
                        <thead>
                            <tr style="background-color: #4A90E2; color: white;">
                                <th style="padding: 8px; text-align: left;">Title</th>
                                <th style="padding: 8px; text-align: left;">Status</th>
                            </tr>
                        </thead>
                        <tbody>
                    """);

            for (DailyReportEvent.TaskInfo task : tasks) {
                taskRows.append("""
                        <tr style="border-bottom: 1px solid #ddd;">
                            <td style="padding: 8px;">%s</td>
                            <td style="padding: 8px;">%s</td>
                        </tr>
                        """.formatted(
                        task.title(),
                        task.status()
                ));
            }

            taskRows.append("</tbody></table>");
        }

        return """
                <html>
                <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <h2 style="color: #4A90E2;">Your Daily Task Report 📋</h2>
                    <p>Hi <strong>%s</strong>,</p>
                    <p>Here's a summary of your tasks:</p>
                    %s
                    <br>
                    <p>Best regards,<br>Task Tracker Team</p>
                </body>
                </html>
                """.formatted(email, taskRows);
    }
}
