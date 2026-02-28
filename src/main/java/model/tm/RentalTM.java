package model.tm;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
@Data
@NoArgsConstructor
@ToString
public class RentalTM {

        private String bid;
        private String id;
        private LocalDate issuedate;
        private LocalDate returndate;

        public RentalTM(String bid, String id, LocalDate issuedate, LocalDate returndate) {
            this.bid = bid;
            this.id = id;
            this.issuedate= issuedate;
            this.returndate = returndate ;
        }
}
