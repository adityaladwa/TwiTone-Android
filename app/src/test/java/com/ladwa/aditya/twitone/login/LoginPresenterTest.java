package com.ladwa.aditya.twitone.login;

import android.util.Log;

import com.ladwa.aditya.twitone.util.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import twitter4j.AsyncTwitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Aditya on 20-Oct-16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Observable.class, AndroidSchedulers.class, RequestToken.class, Log.class})
public class LoginPresenterTest {
    private LoginPresenter mLoginPresenter;

    @Mock
    private LoginContract.View view;

    @Mock
    private AsyncTwitter asyncTwitter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideRule = new RxSchedulersOverrideRule();

    @Captor
    private ArgumentCaptor<Subscriber<RequestToken>> requestTokenCaptor;

    @Captor
    private ArgumentCaptor<Subscriber<AccessToken>> accessTokenCaptor;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mLoginPresenter = spy(new LoginPresenter(view, asyncTwitter));

    }

    @Test
    public void testShouldScheduleRequestTokenFromBackgroundThread() {
        Observable<RequestToken> requestTokenObservable = (Observable<RequestToken>) mock(Observable.class);

        Subscription subscription = mock(Subscription.class);
        RequestToken token = mock(RequestToken.class);

        when(mLoginPresenter.getRequestTokenObservable()).thenReturn(requestTokenObservable);
        when(requestTokenObservable.subscribeOn(Schedulers.newThread())).thenReturn(requestTokenObservable);
        when(requestTokenObservable.observeOn(AndroidSchedulers.mainThread())).thenReturn(requestTokenObservable);
        when(requestTokenObservable.subscribe(requestTokenCaptor.capture())).thenReturn(subscription);


        mLoginPresenter.login();


        verify(mLoginPresenter).getRequestTokenObservable();
        verify(requestTokenObservable).subscribeOn(Schedulers.io());
        verify(requestTokenObservable).observeOn(AndroidSchedulers.mainThread());
        verify(requestTokenObservable).subscribe(requestTokenCaptor.capture());

        requestTokenCaptor.getValue().onNext(token);
        requestTokenCaptor.getValue().onCompleted();
        requestTokenCaptor.getValue().onError(mock(Throwable.class));

    }

    @Test
    public void testShouldScheduleAccessTokenFromBackgroundThread() {
        Observable<AccessToken> accessTokenObservable = (Observable<AccessToken>) mock(Observable.class);
        Subscription subscription = mock(Subscription.class);
        AccessToken token = mock(AccessToken.class);


        when(mLoginPresenter.getAccessTokenObservable(mock(String.class))).thenReturn(accessTokenObservable);
        when(accessTokenObservable.subscribeOn(Schedulers.newThread())).thenReturn(accessTokenObservable);
        when(accessTokenObservable.observeOn(AndroidSchedulers.mainThread())).thenReturn(accessTokenObservable);
        when(accessTokenObservable.subscribe(accessTokenCaptor.capture())).thenReturn(subscription);

        mLoginPresenter.getAccessToken(mock(String.class));

        verify(mLoginPresenter).getAccessTokenObservable(mock(String.class));
        verify(accessTokenObservable).subscribeOn(Schedulers.io());
        verify(accessTokenObservable).observeOn(AndroidSchedulers.mainThread());
        verify(accessTokenObservable).subscribe(accessTokenCaptor.capture());
        mockStatic(Log.class);

        accessTokenCaptor.getValue().onNext(token);
        accessTokenCaptor.getValue().onCompleted();
        accessTokenCaptor.getValue().onError(mock(Throwable.class));

    }


}
