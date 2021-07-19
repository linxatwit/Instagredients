package edu.wit.mobileapp.instagredients;

import java.util.List;

public class Recipes {
    private String link;
    private String title;
    private List<String> ingredientsArray;

    @Override
    public String toString() {
        return "Recipes{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", ingredientsArray=" + ingredientsArray +
                '}';
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIngredientsArray(List<String> ingredientsArray) {
        this.ingredientsArray = ingredientsArray;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getIngredientsArray() {
        return ingredientsArray;
    }
}
