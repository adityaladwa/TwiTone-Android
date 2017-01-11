package com.ladwa.aditya.twitone.data.remote;

import android.content.SharedPreferences;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.TwitterDataStore;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.data.local.models.Interaction;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.util.Utility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import twitter4j.GeoLocation;
import twitter4j.Location;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;

/**
 * A class that fetches Data from Remote Web service
 * Created by Aditya on 04-Jul-16.
 */
public class TwitterRemoteDataSource implements TwitterDataStore {

    @Inject public Twitter mTwitter;
    @Inject SharedPreferences preferences;
    @Inject TwitterLocalDataStore mTwitterLocalDataStore;
    long id;

    public TwitterRemoteDataSource() {
        TwitoneApp.getTwitterComponent().inject(this);
        id = preferences.getLong(TwitoneApp.getInstance().getString(R.string.pref_userid), 0);
        String token = preferences.getString(TwitoneApp.getInstance().getString(R.string.pref_access_token), "");
        String secret = preferences.getString(TwitoneApp.getInstance().getString(R.string.pref_access_secret), "");
        AccessToken accessToken = new AccessToken(token, secret);
        mTwitter.setOAuthAccessToken(accessToken);
    }


    @Override
    public Observable<com.ladwa.aditya.twitone.data.local.models.User> getUserInfo(final long userID) {
        final com.ladwa.aditya.twitone.data.local.models.User localUser = new com.ladwa.aditya.twitone.data.local.models.User();
        return Observable.create(new Observable.OnSubscribe<com.ladwa.aditya.twitone.data.local.models.User>() {
            @Override
            public void call(Subscriber<? super com.ladwa.aditya.twitone.data.local.models.User> subscriber) {
                try {
//                    Timber.d(String.valueOf(userID));
                    User user = mTwitter.showUser(userID);
                    localUser.setId(user.getId());
                    localUser.setName(user.getName());
                    localUser.setScreenName(user.getScreenName());
                    localUser.setProfileUrl(user.getOriginalProfileImageURL());
                    localUser.setBannerUrl(user.getProfileBannerMobileRetinaURL());
                    localUser.setLastModified(Utility.getDateTime());


                    subscriber.onNext(localUser);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }

        }).doOnNext(new Action1<com.ladwa.aditya.twitone.data.local.models.User>() {
            @Override
            public void call(com.ladwa.aditya.twitone.data.local.models.User user) {
                mTwitterLocalDataStore.saveUserInfo(user);
            }
        });
    }

    @Override
    public Observable<List<Tweet>> getTimeLine(final long sinceId) {
        final List<Tweet> localTweet = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<List<Tweet>>() {
            @Override
            public void call(Subscriber<? super List<Tweet>> subscriber) {
                try {

                    Paging p = new Paging();
                    p.setCount(100);
                    if (sinceId > 1)
                        p.setSinceId(sinceId);
                    ResponseList<Status> homeTimeline = mTwitter.getHomeTimeline(p);
                    for (Status status : homeTimeline) {
                        Tweet tweet = new Tweet();
                        MediaEntity[] mediaEntities = status.getMediaEntities();
                        for (MediaEntity m : mediaEntities) {
                            String mediaURLHttps = m.getMediaURLHttps();
                            String type = m.getType();
                            if (type.equals("photo")) {
                                tweet.setMediaUrl(mediaURLHttps);
                            }
                        }
                        tweet.setTweet(status.getText());
                        tweet.setId(status.getId());
                        tweet.setDateCreated(String.valueOf(status.getCreatedAt()));
                        tweet.setLastModified(Utility.getDateTime());
                        tweet.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                        tweet.setScreenName(status.getUser().getScreenName());
                        tweet.setUserName(status.getUser().getName());
                        tweet.setFavCount(status.getFavoriteCount());
                        tweet.setRetweetCount(status.getRetweetCount());
                        tweet.setVerified(status.getUser().isVerified() ? 1 : 0);
                        tweet.setFav(status.isFavorited() ? 1 : 0);
                        tweet.setRetweet(status.isRetweetedByMe() ? 1 : 0);

                        localTweet.add(tweet);

                    }
                    subscriber.onNext(localTweet);


                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<List<Tweet>>() {
            @Override
            public void call(List<Tweet> tweets) {
                mTwitterLocalDataStore.saveTimeLine(tweets);
            }
        });
    }



    public Observable<List<Tweet>> getTimeLineBelow(final long maxId) {
        final List<Tweet> localTweet = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<List<Tweet>>() {
            @Override
            public void call(Subscriber<? super List<Tweet>> subscriber) {
                try {

                    Paging p = new Paging();
                    p.setCount(20);
                    if (maxId > 1)
                        p.setMaxId(maxId);
                    ResponseList<Status> homeTimeline = mTwitter.getHomeTimeline(p);
                    for (Status status : homeTimeline) {
                        Tweet tweet = new Tweet();
                        MediaEntity[] mediaEntities = status.getMediaEntities();
                        for (MediaEntity m : mediaEntities) {
                            String mediaURLHttps = m.getMediaURLHttps();
                            String type = m.getType();
                            if (type.equals("photo")) {
                                tweet.setMediaUrl(mediaURLHttps);
                            }
                        }
                        tweet.setTweet(status.getText());
                        tweet.setId(status.getId());
                        tweet.setDateCreated(String.valueOf(status.getCreatedAt()));
                        tweet.setLastModified(Utility.getDateTime());
                        tweet.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                        tweet.setScreenName(status.getUser().getScreenName());
                        tweet.setUserName(status.getUser().getName());
                        tweet.setFavCount(status.getFavoriteCount());
                        tweet.setRetweetCount(status.getRetweetCount());
                        tweet.setVerified(status.getUser().isVerified() ? 1 : 0);
                        tweet.setFav(status.isFavorited() ? 1 : 0);
                        tweet.setRetweet(status.isRetweetedByMe() ? 1 : 0);

                        localTweet.add(tweet);

                    }
                    subscriber.onNext(localTweet);


                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<List<Tweet>>() {
            @Override
            public void call(List<Tweet> tweets) {
                mTwitterLocalDataStore.saveTimeLine(tweets);
            }
        });
    }

    @Override
    public Observable<List<Interaction>> getInteraction(final long sinceId) {
        final List<Interaction> localInteraction = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<List<Interaction>>() {
            @Override
            public void call(Subscriber<? super List<Interaction>> subscriber) {

                try {
                    Paging p = new Paging();
                    p.setCount(50);
                    if (sinceId > 1)
                        p.setSinceId(sinceId);
                    ResponseList<Status> mentionsTimeline = mTwitter.getMentionsTimeline(p);
                    for (Status status : mentionsTimeline) {
                        Interaction interaction = new Interaction();
                        interaction.setTweet(status.getText());
                        interaction.setId(status.getId());
                        interaction.setDateCreated(String.valueOf(status.getCreatedAt()));
                        interaction.setLastModified(Utility.getDateTime());
                        interaction.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                        interaction.setScreenName(status.getUser().getScreenName());
                        interaction.setUserName(status.getUser().getName());
                        interaction.setFavCount(status.getFavoriteCount());
                        interaction.setRetweetCount(status.getRetweetCount());
                        interaction.setVerified(status.getUser().isVerified() ? 1 : 0);
                        interaction.setFav(status.isFavorited() ? 1 : 0);
                        interaction.setRetweet(status.isRetweetedByMe() ? 1 : 0);

                        localInteraction.add(interaction);

                    }
                    subscriber.onNext(localInteraction);


                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }

            }
        }).doOnNext(new Action1<List<Interaction>>() {
            @Override
            public void call(List<Interaction> interactions) {
                mTwitterLocalDataStore.saveInteraction(interactions);
            }
        });
    }

    @Override
    public Observable<List<DirectMessage>> getDirectMessage(final long sinceId) {
        final List<DirectMessage> localDirectMessages = new ArrayList<>();

        return Observable.create(new Observable.OnSubscribe<List<DirectMessage>>() {
            @Override
            public void call(Subscriber<? super List<DirectMessage>> subscriber) {
                try {
                    Paging p = new Paging();
                    p.setCount(50);
                    if (sinceId > 1)
                        p.setSinceId(sinceId);
                    ResponseList<twitter4j.DirectMessage> directMessages = mTwitter.getDirectMessages(p);
                    ResponseList<twitter4j.DirectMessage> sentMessages = mTwitter.getSentDirectMessages(p);
                    directMessages.addAll(sentMessages);
//                    Timber.d(String.valueOf(directMessages.size()));
                    for (twitter4j.DirectMessage message : directMessages) {
                        DirectMessage directMessage = new DirectMessage();

                        directMessage.setId(message.getId());
                        directMessage.setDateCreated(String.valueOf(message.getCreatedAt()));
                        directMessage.setRecipient(message.getRecipient().getName());
                        directMessage.setRecipientId(message.getRecipientId());
                        directMessage.setRecipientScreenName(message.getRecipientScreenName());
                        directMessage.setSender(message.getSender().getName());
                        directMessage.setSenderId(message.getSenderId());
                        directMessage.setSenderScreenName(message.getSenderScreenName());
                        directMessage.setText(message.getText());
                        directMessage.setProfileUrl(message.getSender().getOriginalProfileImageURL());

                        localDirectMessages.add(directMessage);
                    }
                    subscriber.onNext(localDirectMessages);


                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }


            }
        }).doOnNext(new Action1<List<DirectMessage>>() {
            @Override
            public void call(List<DirectMessage> directMessageList) {
                mTwitterLocalDataStore.saveDirectMessage(directMessageList);
            }
        });


    }

    @Override
    public Observable<List<com.ladwa.aditya.twitone.data.local.models.Trend>> getTrends() {
        final List<com.ladwa.aditya.twitone.data.local.models.Trend> localTrendObject = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<List<com.ladwa.aditya.twitone.data.local.models.Trend>>() {
            @Override
            public void call(Subscriber<? super List<com.ladwa.aditya.twitone.data.local.models.Trend>> subscriber) {
                Trends placeTrends = null;
                try {
                    placeTrends = mTwitter.getPlaceTrends(1);
                    Trend[] trends = placeTrends.getTrends();
                    for (Trend trend : trends) {
                        com.ladwa.aditya.twitone.data.local.models.Trend trend1 = new com.ladwa.aditya.twitone.data.local.models.Trend();
                        trend1.setTrend(trend.getName());
                        trend1.setLocal(0);
                        trend1.setDateCreated(Utility.getDateTime());

                        localTrendObject.add(trend1);
                    }
                    subscriber.onNext(localTrendObject);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }

            }
        }).doOnNext(new Action1<List<com.ladwa.aditya.twitone.data.local.models.Trend>>() {
            @Override
            public void call(List<com.ladwa.aditya.twitone.data.local.models.Trend> trendList) {
                //Delete Old trends first
                mTwitterLocalDataStore.deleteTrends();
            }
        }).doOnNext(new Action1<List<com.ladwa.aditya.twitone.data.local.models.Trend>>() {
            @Override
            public void call(List<com.ladwa.aditya.twitone.data.local.models.Trend> trendList) {
                mTwitterLocalDataStore.saveTrend(trendList);
            }
        });
    }

    @Override
    public Observable<List<com.ladwa.aditya.twitone.data.local.models.Trend>> getLocalTrends(final double latitude, final double longitude) {
        final List<com.ladwa.aditya.twitone.data.local.models.Trend> localTrendObject = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<List<com.ladwa.aditya.twitone.data.local.models.Trend>>() {
            @Override
            public void call(Subscriber<? super List<com.ladwa.aditya.twitone.data.local.models.Trend>> subscriber) {
                Trends placeTrends = null;
                try {
                    ResponseList<Location> closestTrends = mTwitter.getClosestTrends(new GeoLocation(latitude, longitude));
                    for (Location loc : closestTrends) {
//                        Timber.d(loc.getName() + "--" + loc.getWoeid());
                    }

                    placeTrends = mTwitter.getPlaceTrends(closestTrends.get(0).getWoeid());
                    Trend[] trends = placeTrends.getTrends();


                    for (Trend trend : trends) {
                        com.ladwa.aditya.twitone.data.local.models.Trend trend1 = new com.ladwa.aditya.twitone.data.local.models.Trend();
                        trend1.setTrend(trend.getName());
                        trend1.setLocal(1);
                        trend1.setDateCreated(Utility.getDateTime());

                        localTrendObject.add(trend1);
                    }
                    subscriber.onNext(localTrendObject);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }

            }
        }).doOnNext(new Action1<List<com.ladwa.aditya.twitone.data.local.models.Trend>>() {
            @Override
            public void call(List<com.ladwa.aditya.twitone.data.local.models.Trend> trendList) {
                //Delete Old trends first
                mTwitterLocalDataStore.deleteLocalTrends();
            }
        }).doOnNext(new Action1<List<com.ladwa.aditya.twitone.data.local.models.Trend>>() {
            @Override
            public void call(List<com.ladwa.aditya.twitone.data.local.models.Trend> trendList) {
                mTwitterLocalDataStore.saveLocalTrend(trendList);
            }
        });
    }


    public Observable<DirectMessage> sendDirectMessage(final long senderId, final String text) {

        return Observable.create(new Observable.OnSubscribe<DirectMessage>() {
            @Override
            public void call(Subscriber<? super DirectMessage> subscriber) {
                final DirectMessage directMessage = new DirectMessage();
                try {
                    twitter4j.DirectMessage message = mTwitter.sendDirectMessage(senderId, text);

                    directMessage.setId(message.getId());
                    directMessage.setDateCreated(String.valueOf(message.getCreatedAt()));
                    directMessage.setRecipient(message.getRecipient().getName());
                    directMessage.setRecipientId(message.getRecipientId());
                    directMessage.setRecipientScreenName(message.getRecipientScreenName());
                    directMessage.setSender(message.getSender().getName());
                    directMessage.setSenderId(message.getSenderId());
                    directMessage.setSenderScreenName(message.getSenderScreenName());
                    directMessage.setText(message.getText());
                    directMessage.setProfileUrl(message.getSender().getOriginalProfileImageURL());

                    subscriber.onNext(directMessage);

                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<DirectMessage>() {
            @Override
            public void call(DirectMessage directMessage) {
                mTwitterLocalDataStore.saveSingleDirectMessage(directMessage);
            }
        });

    }

    public Observable<Tweet> createFavourite(final long id) {
        return Observable.create(new Observable.OnSubscribe<Tweet>() {
            @Override
            public void call(Subscriber<? super Tweet> subscriber) {
                try {
                    Status status = mTwitter.createFavorite(id);
                    StatusUpdate statusUpdate;
                    Tweet tweet = new Tweet();
                    tweet.setTweet(status.getText());
                    tweet.setId(status.getId());
                    tweet.setDateCreated(String.valueOf(status.getCreatedAt()));
                    tweet.setLastModified(Utility.getDateTime());
                    tweet.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                    tweet.setScreenName(status.getUser().getScreenName());
                    tweet.setUserName(status.getUser().getName());
                    tweet.setFavCount(status.getFavoriteCount());
                    tweet.setRetweetCount(status.getRetweetCount());
                    tweet.setVerified(status.getUser().isVerified() ? 1 : 0);
                    tweet.setFav(status.isFavorited() ? 1 : 0);
                    tweet.setRetweet(status.isRetweetedByMe() ? 1 : 0);

                    subscriber.onNext(tweet);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<Tweet>() {
            @Override
            public void call(Tweet tweet) {
                mTwitterLocalDataStore.createFavourite(tweet);
            }
        });
    }

    public Observable<Tweet> destoryFavourite(final long id) {
        return Observable.create(new Observable.OnSubscribe<Tweet>() {
            @Override
            public void call(Subscriber<? super Tweet> subscriber) {
                try {
                    Status status = mTwitter.destroyFavorite(id);
                    Tweet tweet = new Tweet();
                    tweet.setTweet(status.getText());
                    tweet.setId(status.getId());
                    tweet.setDateCreated(String.valueOf(status.getCreatedAt()));
                    tweet.setLastModified(Utility.getDateTime());
                    tweet.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                    tweet.setScreenName(status.getUser().getScreenName());
                    tweet.setUserName(status.getUser().getName());
                    tweet.setFavCount(status.getFavoriteCount());
                    tweet.setRetweetCount(status.getRetweetCount());
                    tweet.setVerified(status.getUser().isVerified() ? 1 : 0);
                    tweet.setFav(status.isFavorited() ? 1 : 0);
                    tweet.setRetweet(status.isRetweetedByMe() ? 1 : 0);

                    subscriber.onNext(tweet);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<Tweet>() {
            @Override
            public void call(Tweet tweet) {
                mTwitterLocalDataStore.destoryFavourite(tweet);
            }
        });
    }

    public Observable<Tweet> createRetweet(final long id) {
        return Observable.create(new Observable.OnSubscribe<Tweet>() {
            @Override
            public void call(Subscriber<? super Tweet> subscriber) {
                try {
                    Status status = mTwitter.retweetStatus(id);
                    Tweet tweet = new Tweet();
                    tweet.setTweet(status.getText());
                    tweet.setId(status.getId());
                    tweet.setDateCreated(String.valueOf(status.getCreatedAt()));
                    tweet.setLastModified(Utility.getDateTime());
                    tweet.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                    tweet.setScreenName(status.getUser().getScreenName());
                    tweet.setUserName(status.getUser().getName());
                    tweet.setFavCount(status.getFavoriteCount());
                    tweet.setRetweetCount(status.getRetweetCount());
                    tweet.setVerified(status.getUser().isVerified() ? 1 : 0);
                    tweet.setFav(status.isFavorited() ? 1 : 0);
                    tweet.setRetweet(status.isRetweeted() ? 1 : 0);

                    subscriber.onNext(tweet);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<Tweet>() {
            @Override
            public void call(Tweet tweet) {
                mTwitterLocalDataStore.createRetweet(tweet);
            }
        });
    }

    //For Interactons
    public Observable<Interaction> createFavouriteInteraction(final long id) {
        return Observable.create(new Observable.OnSubscribe<Interaction>() {
            @Override
            public void call(Subscriber<? super Interaction> subscriber) {
                try {
                    Status status = mTwitter.createFavorite(id);
                    Interaction interaction = new Interaction();
                    interaction.setTweet(status.getText());
                    interaction.setId(status.getId());
                    interaction.setDateCreated(String.valueOf(status.getCreatedAt()));
                    interaction.setLastModified(Utility.getDateTime());
                    interaction.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                    interaction.setScreenName(status.getUser().getScreenName());
                    interaction.setUserName(status.getUser().getName());
                    interaction.setFavCount(status.getFavoriteCount());
                    interaction.setRetweetCount(status.getRetweetCount());
                    interaction.setVerified(status.getUser().isVerified() ? 1 : 0);
                    interaction.setFav(status.isFavorited() ? 1 : 0);
                    interaction.setRetweet(status.isRetweetedByMe() ? 1 : 0);

                    subscriber.onNext(interaction);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<Interaction>() {
            @Override
            public void call(Interaction interaction) {
                mTwitterLocalDataStore.createFavouriteInteraction(interaction);
            }
        });
    }

    public Observable<Interaction> destoryFavouriteInteraction(final long id) {
        return Observable.create(new Observable.OnSubscribe<Interaction>() {
            @Override
            public void call(Subscriber<? super Interaction> subscriber) {
                try {
                    Status status = mTwitter.destroyFavorite(id);
                    Interaction interaction = new Interaction();
                    interaction.setTweet(status.getText());
                    interaction.setId(status.getId());
                    interaction.setDateCreated(String.valueOf(status.getCreatedAt()));
                    interaction.setLastModified(Utility.getDateTime());
                    interaction.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                    interaction.setScreenName(status.getUser().getScreenName());
                    interaction.setUserName(status.getUser().getName());
                    interaction.setFavCount(status.getFavoriteCount());
                    interaction.setRetweetCount(status.getRetweetCount());
                    interaction.setVerified(status.getUser().isVerified() ? 1 : 0);
                    interaction.setFav(status.isFavorited() ? 1 : 0);
                    interaction.setRetweet(status.isRetweetedByMe() ? 1 : 0);

                    subscriber.onNext(interaction);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<Interaction>() {
            @Override
            public void call(Interaction interaction) {
                mTwitterLocalDataStore.destoryFavouriteInteraction(interaction);
            }
        });
    }

    public Observable<Interaction> createRetweetInteraction(final long id) {
        return Observable.create(new Observable.OnSubscribe<Interaction>() {
            @Override
            public void call(Subscriber<? super Interaction> subscriber) {
                try {
                    Status status = mTwitter.retweetStatus(id);
                    Interaction interaction = new Interaction();
                    interaction.setTweet(status.getText());
                    interaction.setId(status.getId());
                    interaction.setDateCreated(String.valueOf(status.getCreatedAt()));
                    interaction.setLastModified(Utility.getDateTime());
                    interaction.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                    interaction.setScreenName(status.getUser().getScreenName());
                    interaction.setUserName(status.getUser().getName());
                    interaction.setFavCount(status.getFavoriteCount());
                    interaction.setRetweetCount(status.getRetweetCount());
                    interaction.setVerified(status.getUser().isVerified() ? 1 : 0);
                    interaction.setFav(status.isFavorited() ? 1 : 0);
                    interaction.setRetweet(status.isRetweetedByMe() ? 1 : 0);

                    subscriber.onNext(interaction);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<Interaction>() {
            @Override
            public void call(Interaction interaction) {
                mTwitterLocalDataStore.createRetweetInteraction(interaction);
            }
        });
    }

    public Observable<Status> updateStatus(final StatusUpdate statusUpdate) {
        return Observable.create(new Observable.OnSubscribe<Status>() {
            @Override
            public void call(Subscriber<? super Status> subscriber) {
                try {
                    Status status = mTwitter.updateStatus(statusUpdate);
                    subscriber.onNext(status);
                } catch (TwitterException e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
