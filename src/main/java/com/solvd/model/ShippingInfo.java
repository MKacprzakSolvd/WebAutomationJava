package com.solvd.model;

import com.solvd.enums.ShippingMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ShippingInfo {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String company;
    private final String addressLine1;
    @Builder.Default
    private final String addressLine2 = "";
    @Builder.Default
    private final String addressLine3 = "";
    private final String city;
    private final String province;
    private final String postalCode;
    private final String country;
    private final String phoneNumber;
    private final ShippingMethod shippingMethod;
}
