package com.home.coexcleducation.jdo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuizSchemeJdo {

    @JsonProperty("question")
    private String question;

    @JsonProperty("answer1")
    private String answer1;

    @JsonProperty("answer2")
    private String answer2;

    @JsonProperty("answer3")
    private String answer3;

    @JsonProperty("answer4")
    private String answer4;

    @JsonProperty("result")
    private String result;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
