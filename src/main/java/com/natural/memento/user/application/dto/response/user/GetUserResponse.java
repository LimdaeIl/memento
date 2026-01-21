package com.natural.memento.user.application.dto.response.user;

import com.natural.memento.user.domain.entity.User;
import com.natural.memento.user.domain.entity.UserAddress;
import com.natural.memento.user.domain.entity.UserRole;

public record GetUserResponse(
        Long id,
        String email,
        String nickname,
        UserRole userRole,
        Address address

) {

    public static GetUserResponse from(User user) {
        return new GetUserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                Address.from(user.getAddress())
        );
    }

    record Address(
            String city,
            String street,
            String zipcode
    ) {

        public static Address from(UserAddress userAddress) {
            if (userAddress == null) {
                return null;
            }

            return new Address(userAddress.getCity(),
                    userAddress.getStreet(),
                    userAddress.getZipcode()
            );
        }
    }
}
