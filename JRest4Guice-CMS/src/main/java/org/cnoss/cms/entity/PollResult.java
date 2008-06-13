/*
 * @(#)PollResult.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.entity;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.audit.AuditableEntity;

/**
 * 投票结果类
 *
 */
public class PollResult extends AuditableEntity {

    /**
     *
     */
    private static final long serialVersionUID = 2077345799653039751L;
    private int               optionId;
    private String            optionText;
    private Poll              poll;
    private int               pollId;
    private int               votes;    // 得票数

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
