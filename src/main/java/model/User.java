package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private int userid;
    private String username;
    private String password;
    private String gender;

}
