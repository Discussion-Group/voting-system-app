package com.discussiongroup.voting;

public class Election {
    private int electionId;
    private String electionName;
    private Integer startTimestamp, endTimestamp;

    Election(int elId, String elName, Integer start, Integer end) {
        this.electionId = elId;
        this.electionName = elName;
        this.startTimestamp = start;
        this.endTimestamp = end;
    }


    int getElectionId() {
        return electionId;
    }

    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }

    String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    Integer getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Integer startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    Integer getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Integer endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}
