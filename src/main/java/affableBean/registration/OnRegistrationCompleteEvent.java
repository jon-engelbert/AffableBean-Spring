package affableBean.registration;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import affableBean.domain.Member;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private final Locale locale;
    private final Member member;

    public OnRegistrationCompleteEvent(final Member member, final Locale locale, final String appUrl) {
        super(member);
        this.member = member;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    //

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public Member getMember() {
        return member;
    }

}