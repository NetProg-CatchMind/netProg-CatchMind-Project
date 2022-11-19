package Client;

import java.security.Provider;
import java.util.Vector;

public class Room {//대화방의 정보표현 객체


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMemberCnt() {
        return memberCnt;
    }

    public void setMemberCnt(int memberCnt) {
        this.memberCnt = memberCnt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Vector<Provider.Service> getUserV() {
        return userV;
    }

    public void setUserV(Vector<Provider.Service> userV) {
        this.userV = userV;
    }

    private String title;

    private int memberCnt;

    private String subject;

    Vector<Provider.Service> userV;//userV: 같은 방에 접속한 Client정보 저장



    public Room() {
        userV = new Vector<>();
    }

}