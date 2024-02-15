package com.danny.shoppingmall.user.repository;

import com.danny.shoppingmall.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByZipcodeAndAddressAndAddressDetail(String zipcode, String address, String addressDetail);
}
