package com.jinhanyu.jack.langren.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.jinhanyu.jack.langren.MainApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anzhuo on 2016/9/10.
 */
public class RoomInfo implements Serializable{


    //voteRecord:[{fromUserId,toUserId}], vote:{userId:voteCount}, voteCounter // 投票计数器，都投完票就计算投票结果
    // users:[{userId, socket, dead, type}],   const hasSaved, const hasPoisoned, const lastGuardedUserId

    private  String roomId;
    private int maxCount;
    private int currentCount;
    private String name;
    private boolean hasSaved;
    private boolean hasPoisoned;
    private String  lastGuardedUserId;
    private String policeId;

    public String getPoliceId() {
        return policeId;
    }

    public void setPoliceId(String policeId) {
        this.policeId = policeId;
    }

    public  UserInfo findUserInRoom(String userId){
        for (UserInfo info : users) {
            if (info.getUserId().equals(userId)) {
                return info;
            }
        }
        throw new RuntimeException("客户端：  用户未找到");
    }

    public  UserInfo findMeInRoom(){
        for (UserInfo info : users) {
            if (info.getUserId().equals(MainApplication.userInfo.getUserId())) {
                return info;
            }
        }
        throw new RuntimeException("客户端：  房间里没有我");
    }

    public     int    findUserIndexInRoom(String userId){
        for (int i=0;i<users.size();i++) {
            if (users.get(i).getUserId().equals(userId)) {
                return i;
            }
        }
        throw new RuntimeException("客户端：  用户未找到");
    }

    public int    findMyIndexInRoom(){
        for (int i=0;i<users.size();i++) {
            if (users.get(i).getUserId().equals(MainApplication.userInfo.getUserId())) {
                return i;
            }
        }
        throw new RuntimeException("客户端：  房间里没有我");
    }


    public boolean isHasPoisoned() {
        return hasPoisoned;
    }

    public void setHasPoisoned(boolean hasPoisoned) {
        this.hasPoisoned = hasPoisoned;
    }

    public String getLastGuardedUserId() {
        return lastGuardedUserId;
    }

    public void setLastGuardedUserId(String lastGuardedUserId) {
        this.lastGuardedUserId = lastGuardedUserId;
    }

    public boolean isHasSaved() {
        return hasSaved;
    }

    public void setHasSaved(boolean hasSaved) {
        this.hasSaved = hasSaved;
    }

    @JSONField(name = "vote")
    private List<VoteResult> voteResults = new ArrayList<>();
    private List<UserInfo> users= new ArrayList<UserInfo>();



    public List<UserInfo> getUsers() {
        return users;
    }

    public List<VoteResult> getVoteResults() {
        return voteResults;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }



    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void changePeopleNum(int diff){
        this.currentCount +=diff;
    }


    public static final int VOTE_POLICE = 0;
    public static final int VOTE_WOLF = 1;
    public static final int VOTE_KILL = 2;

    public List<UserInfo> getAliveUsers() {
        List<UserInfo> userInfos = new ArrayList<>();
        for(UserInfo userInfo: users)
        {
            if(!userInfo.getGameRole().isDead())
                userInfos.add(userInfo);
        }
        return userInfos;
    }

    public void unreadyAll() {
        for(UserInfo user: users)
            user.getGameRole().setReady(false);

        setCurrentCount(0);
    }
}
