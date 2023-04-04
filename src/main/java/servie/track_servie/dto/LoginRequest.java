package servie.track_servie.dto;

import lombok.Data;

@Data
public class LoginRequest
{
    private String email;
    private String password;
}
