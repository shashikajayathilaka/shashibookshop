package model.tm;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString

public class BookTM {
    private String bid;
    private String btitle;
    private String author;
    private String category;
    private Integer quantity;

    public BookTM(String bid,String btitle,String author,String category, Integer quantity){
        this.bid= bid;
        this.btitle=btitle;
        this.author=author;
        this.category=category;
        this.quantity=quantity;
    }

}
