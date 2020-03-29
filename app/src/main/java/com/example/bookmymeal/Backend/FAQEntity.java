package com.example.bookmymeal.Backend;

public class FAQEntity {
   private int id;
   private String question;
   private String solution;

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getSolution() {
        return solution;
    }
}
