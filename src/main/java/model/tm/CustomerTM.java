package model.tm;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class CustomerTM {
    private String id;
    private String name;
    private String address;
    private String telephoneno;

    public CustomerTM(String id, String name, String address, String telephoneno) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.telephoneno = telephoneno;
    }
}
