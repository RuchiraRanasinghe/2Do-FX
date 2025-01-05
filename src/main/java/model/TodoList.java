package model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TodoList {
    private int taskid;
    private int userid;
    private String task;
    private Date datecreated;
    private String discription;
}
