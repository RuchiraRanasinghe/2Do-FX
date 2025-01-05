package model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task {
    private int userid;
    private String task;
    private Date datecreated;
    private String description;
}
