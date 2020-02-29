package ke.co.simpledeveloper.adapters;

import androidx.annotation.NonNull;

public class ExpandableObject {

    private String question;

    private String answer;

    private boolean expanded;

    public ExpandableObject(){}

    public ExpandableObject(String question, String answer, boolean expanded) {
        this.question = question;
        this.answer = answer;
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
