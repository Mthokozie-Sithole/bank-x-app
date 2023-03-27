package com.banking.bankingapp.util;

import com.banking.bankingapp.model.constants.TransactionType;
import com.banking.bankingapp.model.dto.request.Notification;
import com.banking.bankingapp.model.dto.request.PaymentRequest;
import com.banking.bankingapp.model.dto.request.TransactionRequest;
import com.banking.bankingapp.model.dto.request.TransferRequest;

import java.time.LocalDate;

public class NotificationHelper {

    private static Notification buildNotification(String affectedAccountNumber, Double amount,
                                                  String subject, String content,
                                                  TransactionType transactionType) {

        Notification notification = new Notification(affectedAccountNumber,
                amount, subject, content, transactionType, LocalDate.now());

        return notification;
    }

    public static Notification buildCreditNotification(TransactionRequest transactionRequest,
                                                       String creditNotification,
                                                       String creditSubject) {
        final String notificationBody = creditNotification
                .replace("{0}", String.valueOf(transactionRequest.getAmount()))
                .replace("{1}", transactionRequest.getAccountNumber());

        Notification notification = buildNotification(transactionRequest.getAccountNumber(),
                transactionRequest.getAmount(), creditSubject, notificationBody, TransactionType.CREDIT);

        System.out.println("Notification: " + notification);
        return notification;
    }

    public static Notification buildDebitNotification(TransactionRequest transactionRequest,
                                                      String debitNotification,
                                                      String debitSubject) {

        final String notificationBody = debitNotification
                .replace("{0}", String.valueOf(transactionRequest.getAmount()))
                .replace("{1}", transactionRequest.getAccountNumber());

        Notification notification = NotificationHelper.buildNotification(transactionRequest.getAccountNumber(),
                transactionRequest.getAmount(), debitSubject, notificationBody, TransactionType.DEBIT);
        return notification;
    }

    public static Notification buildFundTransferNotification(TransferRequest transferRequest,
                                                             String fundTransferNotification,
                                                             String transferSubject) {

        final String notificationBody = fundTransferNotification
                .replace("{0}", String.valueOf(transferRequest.getTransferAmount()))
                .replace("{1}", transferRequest.getFromAccount())
                .replace("{2}", transferRequest.getToAccount());

        Notification notification = NotificationHelper
                .buildNotification(transferRequest.getFromAccount(),
                        transferRequest.getTransferAmount(), transferSubject,
                        notificationBody,
                        TransactionType.TRANSFER);
        return notification;
    }

    public static Notification buildPaymentNotification(PaymentRequest paymentRequest,
                                                        String paymentNotification,
                                                        String paymentSubject) {

        final String notificationBody = paymentNotification
                .replace("{0}", String.valueOf(paymentRequest.getPaymentAmount()))
                .replace("{1}", paymentRequest.getAccountNumber());

        Notification notification = buildNotification(paymentRequest.getAccountNumber(),
                paymentRequest.getPaymentAmount(), paymentSubject,
                notificationBody,
                TransactionType.INCOMING_PAYMENT);
        return notification;
    }

}
