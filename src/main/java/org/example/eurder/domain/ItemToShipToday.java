package org.example.eurder.domain;

import org.example.eurder.domain.user.Address;

public record ItemToShipToday(
        ItemGroup group,
        Address address
) {
}
