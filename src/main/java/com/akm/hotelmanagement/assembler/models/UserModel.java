package com.akm.hotelmanagement.assembler.models;

import com.akm.hotelmanagement.dto.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel extends RepresentationModel<UserModel> {
    private UUID id;
    private String name;
    private String email;
    private String username;
    private String phone;

    public UserModel(UserResponseDto dto) {
        this(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getUsername(),
                dto.getPhone()
        );
    }
}
