package duancuoiky.lechihai.trochoidoantendongvat;

import java.util.List;

public class Animal {
    public String name;
    public String image;
    public String info;
    public List<String> wrongAnswers;

    public Animal() {
        // Constructor rỗng bắt buộc cho Firebase
    }

    public Animal(String name, String image, String info, List<String> wrongAnswers) {
        this.name = name;
        this.image = image;
        this.info = info;
        this.wrongAnswers = wrongAnswers;
    }
}