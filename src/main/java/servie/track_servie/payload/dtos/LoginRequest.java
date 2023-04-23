package servie.track_servie.payload.dtos;

import lombok.Data;

@Data
public class LoginRequest
{
    private String email;
    private String password;
}
