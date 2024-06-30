package dong.datn.tourify.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object MailSender {
    private var receiverEmail: String = ""
    private var emailSubject: String = ""
    private var emailContent: String = ""
    private var onSuccessCallback: (() -> Unit)? = null
    private var onFailCallback: ((Exception) -> Unit)? = null

    private var senderEmail: String = "20A10010326@students.hou.edu.vn"
    private var senderPassword: String = "mguraesylquvhydt"
    private var host: String = "smtp.gmail.com"


    fun init(stringSenderEmail: String, stringPasswordSenderEmail: String, stringHost: String): MailSender {
        this.senderEmail = stringSenderEmail
        this.senderPassword = stringPasswordSenderEmail
        this.host = stringHost
        return this
    }
    fun to(email: String): MailSender {
        this.receiverEmail = email
        return this
    }

    fun subject(subject: String): MailSender {
        this.emailSubject = subject
        return this
    }

    fun body(content: String): MailSender {
        this.emailContent = content
        return this
    }

    fun success(callback: () -> Unit): MailSender {
        this.onSuccessCallback = callback
        return this
    }

    fun fail(callback: (Exception) -> Unit): MailSender {
        this.onFailCallback = callback
        return this
    }

    fun send() {


        if (receiverEmail.isEmpty()) {
            println("Receiver email is empty")
            onFailCallback?.invoke(Exception("Receiver email is empty"))
            return
        }

        val properties = Properties().apply {
            setProperty("mail.transport.protocol", "smtp")
            setProperty("mail.host", host)
            put("mail.smtp.host", host)
            put("mail.smtp.port", "465")
            put("mail.smtp.socketFactory.fallback", "false")
            setProperty("mail.smtp.quitwait", "false")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.ssl.enable", "true")
            put("mail.smtp.auth", "true")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, senderPassword)
            }
        })

        try {
            val mimeMessage = MimeMessage(session).apply {
                addRecipient(Message.RecipientType.TO, InternetAddress(receiverEmail))
                subject = emailSubject
                setText(emailContent)
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Transport.send(mimeMessage)
                    println("Email sent successfully")
                    withContext(Dispatchers.Main) { onSuccessCallback?.invoke() }
                } catch (e: MessagingException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) { onFailCallback?.invoke(e) }
                }
            }
        } catch (e: MessagingException) {
            e.printStackTrace()
            onFailCallback?.invoke(e)
        }
    }
}
