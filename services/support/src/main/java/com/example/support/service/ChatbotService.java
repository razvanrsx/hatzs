package com.example.support.service;

import com.example.support.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class ChatbotService {

    private static final List<ChatRule> RULES = List.of(
            new ChatRule("greeting", Pattern.compile("\\b(hi|hello|hey)\\b", Pattern.CASE_INSENSITIVE),
                    "Hello! How can I assist you with your energy management account today?"),
            new ChatRule("login-help", Pattern.compile("\\b(log\\s*in|signin|sign in)\\b", Pattern.CASE_INSENSITIVE),
                    "If you're having trouble logging in, verify your username and reset the password if needed."),
            new ChatRule("password-reset", Pattern.compile("\\b(password|reset|forgot)\\b", Pattern.CASE_INSENSITIVE),
                    "You can reset your password from the login screen using the 'Forgot password' option."),
            new ChatRule("register", Pattern.compile("\\b(register|sign\\s*up|create account)\\b", Pattern.CASE_INSENSITIVE),
                    "To register, open the sign-up form and provide your email, username, and a secure password."),
            new ChatRule("add-device", Pattern.compile("\\b(add|register)\\s+device\\b", Pattern.CASE_INSENSITIVE),
                    "Go to Devices and click 'Add device' to register a new meter with its description."),
            new ChatRule("assign-device", Pattern.compile("\\b(assign|owner|ownership)\\b", Pattern.CASE_INSENSITIVE),
                    "Admins can assign devices to users from the Devices section by selecting an owner."),
            new ChatRule("overconsumption", Pattern.compile("\\b(overconsumption|alert|threshold)\\b",
                    Pattern.CASE_INSENSITIVE),
                    "Overconsumption alerts trigger when hourly usage exceeds the configured threshold."),
            new ChatRule("billing", Pattern.compile("\\b(billing|payment|invoice)\\b", Pattern.CASE_INSENSITIVE),
                    "Billing details are handled by your organization. Contact finance for invoices or payments."),
            new ChatRule("support-hours", Pattern.compile("\\b(hours|availability|open)\\b", Pattern.CASE_INSENSITIVE),
                    "Support is available Monday to Friday, 09:00-17:00 local time."),
            new ChatRule("contact", Pattern.compile("\\b(contact|email|phone)\\b", Pattern.CASE_INSENSITIVE),
                    "You can reach support at support@example.com for escalations.")
    );

    public ChatResponse respond(String message) {
        String safeMessage = message == null ? "" : message.toLowerCase(Locale.ROOT);
        for (ChatRule rule : RULES) {
            if (rule.pattern().matcher(safeMessage).find()) {
                return new ChatResponse(rule.response(), rule.name());
            }
        }
        return new ChatResponse(
                "I'm not sure about that yet. Please describe the issue in more detail or contact support.",
                "fallback"
        );
    }

    private record ChatRule(String name, Pattern pattern, String response) {
    }
}
