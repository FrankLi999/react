package com.bpwizard.configjdbc.core.service;

/**
 * The mail sender interface for sending mail
 */
public interface MailSender<MailData> {

    void send(MailData mail);
}
