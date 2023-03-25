package com.banking.bankingapp.util;

import com.banking.bankingapp.model.constants.TransactionType;
import com.banking.bankingapp.model.dto.request.Notification;

import java.time.LocalDate;

public class NotificationHelper {

    public static Notification buildNotification(String affectedAccountNumber, Double amount,
                                                 String subject, String content,
                                                 TransactionType transactionType) {

        Notification notification = new Notification(affectedAccountNumber,
                amount, subject, content, transactionType, LocalDate.now());

        return notification;

    }

}
