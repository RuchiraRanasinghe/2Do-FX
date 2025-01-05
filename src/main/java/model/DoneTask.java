package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DoneTask {
    private int userid;
    private  String task;
    private String datecreated;
    private String discription;
}
