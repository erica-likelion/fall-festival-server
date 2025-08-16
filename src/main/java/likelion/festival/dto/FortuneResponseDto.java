package likelion.festival.dto;


public class FortuneResponseDto {
    private String message;
    private int score;
    private String color;
    private String image_url;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getScore() {
        return score;
    }

    public String getColor() {
        return color;
    }

    public String getImage_url() {
        return image_url;
    }
}
